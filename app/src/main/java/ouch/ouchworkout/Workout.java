package ouch.ouchworkout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ouch.ouchworkout.countdown.AbstractCountdown;
import ouch.ouchworkout.countdown.ActionCountdown;
import ouch.ouchworkout.countdown.AfterCountdown;
import ouch.ouchworkout.countdown.RestCountdown;

public class Workout {
    private static Workout workout = null;
    private final String name;
    private Activity activity;
    private ProgressBar bar;
    private List<Exercise> exercises = new ArrayList<>();
    private int currentIndex;
    private boolean actionPhase, isRunning;
    private AbstractCountdown resumeCd, restCd, actionCd, afterCd, oldAfterCd;

    private Workout(String pName, JSONArray pDesc) throws JSONException {
        name = pName;
        currentIndex = 0;
        actionPhase = false;
        isRunning = false;
        for (int i = 0; i < pDesc.length(); i++) {
            JSONObject info = pDesc.getJSONObject(i);
            Exercise exe = new Exercise(this, info.getString("name"),
                    info.getString("img"), info.getInt("set_nb"),
                    info.getInt("rep_nb"), info.getInt("load_kg"),
                    info.getInt("action_sec"), info.getInt("rest_sec"),
                    info.getInt("after_sec"));
            exercises.add(exe);
        }
    }

    public static Workout createWorkout(String pName, JSONArray pDesc) throws JSONException {
        workout = new Workout(pName, pDesc);
        return workout;
    }

    public static Workout getWorkout() {
        return workout;
    }

    public static boolean hasWorkout() {
        return workout != null;
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isActionPhase() {
        return actionPhase;
    }

    public void initializeWorkout(Activity pAct) {
        activity = pAct;
        // Display the workout name
        TextView nameField = (TextView) findViewById(R.id.exercise_name);
        nameField.setText(name);
        // Display the length of the workout (the sum of exercise lengths) in minutes
        TextView countdownField = (TextView) findViewById(R.id.countdown);
        int length = 0;
        for (int i = currentIndex; i < exercises.size(); i++) {
            length += exercises.get(i).getLengthSeconds();
        }
        int length_minutes = length / 60;
        if (length_minutes < 10) {
            countdownField.setText(String.format("00%d", length / 60));
        } else if (length_minutes < 100) {
            countdownField.setText(String.format("0%d", length / 60));
        } else {
            countdownField.setText(String.format("%d", length / 60));
        }
        // Fix bug: pause the workout during the afterCd
        oldAfterCd = new AfterCountdown(5);
        // Set the length of the progress bar
        bar = (ProgressBar) findViewById(R.id.workout_bar);
        bar.setMax(exercises.size());
        bar.setProgress(currentIndex);
        // Load the first exercise
        loadCurrentExercise();
    }

    public void loadCurrentExercise() {
        // Display the exercise
        Exercise ex = getCurrentExercise();
        ex.display();
        // Configure countdowns for the exercise
        actionCd = new ActionCountdown(ex.getActionTime());
        restCd = new RestCountdown(ex.getRestTime());
        afterCd = new AfterCountdown(ex.getAfterTime());
        resumeCd = new AfterCountdown(10);
    }

    public void decreaseActionNb() {
        getCurrentExercise().decreaseSetNb();
    }

    public void next() {
        Exercise exe = getCurrentExercise();
        if (actionPhase) {
            actionPhase = false;
            if (exe.getSetNb() > 0) {
                restCd.startCountdown();
            } else {
                if (selectNextExercise()) {
                    oldAfterCd = afterCd;
                    oldAfterCd.startCountdown();
                    loadCurrentExercise();
                } else {
                    // The workout is completed
                    TextView nameField = (TextView) findViewById(R.id.exercise_name);
                    nameField.setText("Workout Completed!");
                    ImageView exerciseImage = (ImageView) findViewById(R.id.exercise_img);
                    exerciseImage.setImageResource(R.drawable.completed);
                    bar.incrementProgressBy(1);
                }
            }
        } else {
            actionPhase = true;
            if (exe.isDoneButtonRequired()) {
                // Display the action light
                ImageView actionLight = (ImageView) findViewById(R.id.action_light);
                actionLight.setImageResource(R.drawable.action);
            } else {
                actionCd.startCountdown();
            }
        }
    }

    // Event fired by the playPause/pause button
    public void playPause() {
        if (isRunning) {
            actionPhase = false;
            isRunning = false;
            actionCd.cancel();
            restCd.cancel();
            oldAfterCd.cancel();
            afterCd.cancel();
            resumeCd.cancel();
        } else {
            isRunning = true;
            resumeCd.startCountdown();
        }
    }

    public List<String> getExerciseNames() {
        List<String> names = new LinkedList<>();
        for (Exercise ex : exercises) {
            names.add(ex.getName());
        }
        return names;
    }

    public void removeExerciseFromNames(List<String> pNames) {
        Iterator<Exercise> it = exercises.iterator();
        while (it.hasNext()) {
            Exercise ex = it.next();
            if (pNames.contains(ex.getName())) {
                it.remove();
            }
        }
    }

    public Exercise getCurrentExercise() {
        return exercises.get(currentIndex);
    }

    public boolean hasNextExercise() {
        return currentIndex + 1 < exercises.size();
    }

    public boolean selectNextExercise() {
        if (hasNextExercise()) {
            bar.incrementProgressBy(1);
            currentIndex++;
            return true;
        } else {
            return false;
        }
    }

    // Application Functions
    public Context getApplicationContext() {
        return activity.getApplicationContext();
    }

    public View findViewById(int pId) {
        return activity.findViewById(pId);
    }

    public int findDrawableByName(String pFilename) {
        return activity.getResources().getIdentifier(pFilename, "drawable",
                activity.getPackageName());
    }
}
