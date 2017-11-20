package ouch.ouchworkout;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ouch.ouchworkout.ouch.workout.countdown.AfterCountdown;

public class Workout {
    private final Activity activity;
    private List<Exercise> exercises = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isRunning = false, exerciseIsRunning = false;
    private AfterCountdown newStartCountdown;

    public Workout(Activity pAct, JSONArray pDesc) throws JSONException {
        activity = pAct;
        int length = 0;
        for (int i = 0; i < pDesc.length(); i++) {
            JSONObject info = pDesc.getJSONObject(i);
            Exercise exe = new Exercise(this, info.getString("name"),
                    info.getString("img"), info.getInt("nb_exercise"),
                    info.getInt("nb_rep"), info.getInt("action"),
                    info.getInt("rest"), info.getInt("after"));
            exercises.add(exe);
            length += exe.getLengthSeconds();
        }
        // Countdown before starting (or restarting) the workout
        newStartCountdown = new AfterCountdown(this, 10000);
        // Display the first exercise
        exercises.get(currentIndex).display();
        TextView countdownField = (TextView) activity.findViewById(R.id.countdown);
        countdownField.setText(String.format("%dm", length/60));
    }

    public void startTheExercise() {
        exerciseIsRunning = true;
        if (currentIndex < exercises.size()) {
            Exercise current = exercises.get(currentIndex);
            if (current.getExerciseNb() == 0) {
                currentIndex++;
                startTheExercise();
            } else {
                current.startExercise();
            }
        } else {
            // The workout is completed
        }
    }

    // Event fired by the play/pause button
    public void play() throws JSONException {
        if (isRunning) {
            isRunning = false;
            if (exerciseIsRunning) {
                exerciseIsRunning = false;
                exercises.get(currentIndex).stop();
            } else {
                newStartCountdown.cancel();
            }
        } else {
            isRunning = true;
            // Wait 10s before starting the exercise
            newStartCountdown.startCountdown();
        }
    }

    public boolean hasNext() {
        return currentIndex + 1 < exercises.size();
    }

    public Exercise getNext() throws JSONException {
        if (hasNext()) {
            // Load the next exercise
            return exercises.get(currentIndex + 1);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    // Application Functions
    public Context getApplicationContext() {
        return activity.getApplicationContext();
    }

    public View findViewById(int pId) {
        return activity.findViewById(pId);
    }

    public int findDrawableByName(String pFilename) {
        return activity.getResources().getIdentifier(pFilename, "drawable", activity.getPackageName());
    }
}
