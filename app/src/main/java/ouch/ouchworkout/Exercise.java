package ouch.ouchworkout;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Exercise {
    private final String name, pictureName;
    private final Workout workout;
    private final int actionTime, restTime, afterTime;
    private final int repNb, loadKg, lengthSeconds;
    private final boolean nextButtonRequired, doneButtonRequired;
    private int setNb;

    public Exercise(Workout pWorkout, String pExerciseName, String pImageName, int pSetNb,
                    int pRepNb, int pLoad, int pActionTime, int pRestTime, int pAfterTime) {
        workout = pWorkout;
        name = pExerciseName;
        // If actionTime = 0 then add 'done' button to finish the set
        actionTime = pActionTime;
        doneButtonRequired = actionTime == 0;
        restTime = pRestTime;
        afterTime = pAfterTime;
        // If setNb = 0 then add the 'next' button to finish the exercise
        setNb = pSetNb;
        nextButtonRequired = setNb == 0;
        repNb = pRepNb;
        loadKg = pLoad;
        lengthSeconds = pSetNb * pActionTime + (pSetNb - 1) * pRestTime + pAfterTime;
        if (pImageName == null || pImageName.length() == 0) {
            pictureName = "";
        } else {
            pictureName = pImageName;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isNextButtonRequired() {
        return nextButtonRequired;
    }

    public boolean isDoneButtonRequired() {
        return doneButtonRequired;
    }

    public int getSetNb() {
        return setNb;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
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

    public void decreaseSetNb() {
        setNb--;
        // Display the number of sets
        TextView setNbField = (TextView) workout.findViewById(R.id.set_nb);
        setNbField.setText(String.valueOf(setNb));
    }

    public void display() {
        // Display the name of the exercise
        final TextView nameField = (TextView) workout.findViewById(R.id.exercise_name);
        nameField.setText(name);
        // Display the image of the exercise
        ImageView setImage = (ImageView) workout.findViewById(R.id.exercise_img);
        setImage.setImageResource(workout.findDrawableByName(pictureName));
        // Hide/show the done button
        Button doneButton = (Button) workout.findViewById(R.id.done_button);
        if (doneButtonRequired) {
            doneButton.setVisibility(View.VISIBLE);
        } else {
            doneButton.setVisibility(View.INVISIBLE);
        }
        // Display the number of sets
        TextView setNbField = (TextView) workout.findViewById(R.id.set_nb);
        setNbField.setText(String.valueOf(setNb));
        // Display the number of reps
        TextView repNbField = (TextView) workout.findViewById(R.id.rep_nb);
        repNbField.setText(String.valueOf(repNb));
        // Display the load of the exercise
        if (loadKg > 0) {
            LinearLayout loadContainer = (LinearLayout) workout.findViewById(R.id.load_container);
            loadContainer.setVisibility(View.VISIBLE);
            TextView loadField = (TextView) workout.findViewById(R.id.load_kg);
            loadField.setText(String.valueOf(loadKg));
        } else {
            LinearLayout loadContainer = (LinearLayout) workout.findViewById(R.id.load_container);
            loadContainer.setVisibility(View.INVISIBLE);
        }
        // Display the countdown light
        ImageView actionLight = (ImageView) workout.findViewById(R.id.action_light);
        actionLight.setImageResource(R.drawable.rest);
    }
}
