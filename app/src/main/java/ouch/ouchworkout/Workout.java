package ouch.ouchworkout;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ouch.ouchworkout.countdown.ActionCountdown;
import ouch.ouchworkout.countdown.AfterCountdown;
import ouch.ouchworkout.countdown.CountdownManager;
import ouch.ouchworkout.countdown.RestCountdown;

public class Workout {
    private static Workout workout = null;
    private final String name;
    private Activity activity;
    private List<Exercise> exercises = new ArrayList<>();
    private int currentIndex = 0;
    private CountdownManager countdownManager;

    private Workout(String pName, JSONArray pDesc) throws JSONException {
        name = pName;
        for (int i = 0; i < pDesc.length(); i++) {
            JSONObject info = pDesc.getJSONObject(i);
            Exercise exe = new Exercise(this, info.getString("name"),
                    info.getString("img"), info.getInt("nb_exercise"),
                    info.getInt("nb_rep"), info.getInt("action"),
                    info.getInt("rest"), info.getInt("after"));
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

    public void initializeWorkout(Activity pAct) {
        activity = pAct;
        // Configure the countdown manager with the first exercise
        Exercise myExercise = exercises.get(currentIndex);
        countdownManager = new CountdownManager(new ActionCountdown(myExercise),
                new RestCountdown(myExercise.getRestTime() * 1000),
                new AfterCountdown(myExercise.getAfterTime() * 1000), myExercise);
        // Display the workout name
        TextView nameField = (TextView) findViewById(R.id.exercise_name);
        nameField.setText(name);
        // Display the number of exercises
        TextView exerciseNbField = (TextView) findViewById(R.id.exercise_counter);
        int exerciseNb = getRemainingExerciseNumber();
        if (exerciseNb < 10) {
            exerciseNbField.setText("0" + exerciseNb);
        } else {
            exerciseNbField.setText(String.valueOf(exerciseNb));
        }
        // Display the first exercise
        myExercise.display();
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
    }

    public void nextCountdown() {
        if (!countdownManager.next()) {
            Exercise myExercise = getCurrentExercise();
            if (myExercise.getExerciseNb() > 0) {
                countdownManager = new CountdownManager(new ActionCountdown(myExercise),
                        new RestCountdown(myExercise.getRestTime() * 1000),
                        new AfterCountdown(myExercise.getAfterTime() * 1000),
                        myExercise);
                countdownManager.next();
            } else {
                // The workout is completed
                TextView nameField = (TextView) findViewById(R.id.exercise_name);
                nameField.setText("Workout Completed!");
                ImageView exerciseImage = (ImageView) findViewById(R.id.exercise_img);
                exerciseImage.setImageResource(R.drawable.completed);
                TextView exerciseNbField = (TextView) findViewById(R.id.exercise_counter);
                exerciseNbField.setText("00");
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean isRunning() {
        return countdownManager.isRunning();
    }

    // Event fired by the playPause/pause button
    public void playPause() {
        if (countdownManager.isRunning()) {
            countdownManager.stop();
        } else {
            countdownManager.startNewStartCountdown();
        }
    }

    public Exercise getCurrentExercise() {
        return exercises.get(currentIndex);
    }

    public boolean hasNextExercise() {
        return currentIndex + 1 < exercises.size();
    }

    public Exercise getNextExercise() {
        if (hasNextExercise()) {
            return exercises.get(currentIndex + 1);
        } else {
            throw new IndexOutOfBoundsException("No more exercise");
        }
    }

    public boolean selectNextExercise() {
        if (hasNextExercise()) {
            currentIndex++;
            return true;
        } else {
            return false;
        }
    }

    public int getRemainingExerciseNumber() {
        return exercises.size() - currentIndex;
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
