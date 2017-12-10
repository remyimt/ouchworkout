package ouch.ouchworkout;

import android.widget.ImageView;
import android.widget.TextView;

public class Exercise {
    private final String name, pictureName;
    private final Workout workout;
    private final int actionTime, restTime, afterTime;
    private final int repNb, lengthSeconds;
    private int exerciseNb;

    public Exercise(Workout pWorkout, String pExerciseName, String pImageName, int pExerciseNb,
                    int pRepNb, int pActionTime, int pRestTime, int pAfterTime) {
        workout = pWorkout;
        name = pExerciseName;
        actionTime = pActionTime;
        restTime = pRestTime;
        afterTime = pAfterTime;
        exerciseNb = pExerciseNb;
        repNb = pRepNb;
        lengthSeconds = pExerciseNb * pActionTime + (pExerciseNb - 1) * pRestTime + pAfterTime;
        if (pImageName == null || pImageName.length() == 0) {
            pictureName = "";
        } else {
            pictureName = pImageName;
        }
    }

    public String getName() {
        return name;
    }

    public int getActionTime() {
        return actionTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getAfterTime() {
        return afterTime;
    }

    public int getExerciseNb() {
        return exerciseNb;
    }

    public int decreaseExerciseNb() {
        exerciseNb--;
        return exerciseNb;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
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
}
