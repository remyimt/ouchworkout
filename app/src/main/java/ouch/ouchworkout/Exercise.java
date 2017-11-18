package ouch.ouchworkout;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import ouch.ouchworkout.ouch.workout.countdown.ActionCountdown;
import ouch.ouchworkout.ouch.workout.countdown.RestCountdown;

public class Exercise {
    private final long LAST_BEEP_VALUE = 2000L;
    private final String name, pictureName;
    private final Workout workout;
    private final int repNb, length;
    private int exerciseNb;
    private ActionCountdown actionCountdown;

    public Exercise(Workout pWorkout, String pExerciseName, String pImageName, int pExerciseNb,
                    int pRepNb, int pActionTime, int pRestTime, int pAfterTime) {
        workout = pWorkout;
        name = pExerciseName;
        exerciseNb = pExerciseNb;
        repNb = pRepNb;
        length = pExerciseNb * pActionTime + (pRepNb - 1) + pRestTime;
        if (pImageName == null || pImageName.length() == 0) {
            pictureName = "";
        } else {
            pictureName = pImageName;
        }
        // Configure the countdowns
        actionCountdown = new ActionCountdown(pWorkout, this,
                pActionTime * 1000, pRestTime * 1000,
                pAfterTime * 1000);
    }

    public int getExerciseNb() {
        return exerciseNb;
    }

    public int decreaseExerciseNb() {
        exerciseNb--;
        return exerciseNb;
    }

    public int getLength() {
        return length;
    }

    public void display() {
        // Display the name of the set
        final TextView nameField = (TextView) workout.findViewById(R.id.exercise_name);
        nameField.setText(name);
        ImageView setImage = (ImageView) workout.findViewById(R.id.exercise_img);
        setImage.setImageResource(workout.findDrawableByName(pictureName));
        // Display the number of sets
        final TextView setNbField = (TextView) workout.findViewById(R.id.exercise_nb);
        setNbField.setText(String.valueOf(exerciseNb));
        // Exercise the number of reps
        final TextView repNbField = (TextView) workout.findViewById(R.id.rep_nb);
        repNbField.setText(String.valueOf(repNb));
        // Exercise the countdown
        ImageView actionLight = (ImageView) workout.findViewById(R.id.action_light);
        actionLight.setImageResource(R.drawable.rest);
    }

    public void startExercise() {
        // Start the action period
        actionCountdown.startCountdown();
    }

    public void stop() {
        actionCountdown.stop();
    }
}
