package ouch.ouchworkout.ouch.workout.countdown;


import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class ActionCountdown extends Countdown {
    private final Workout workout;
    private Exercise exercise;
    private Countdown restCountdown;
    private Countdown afterCountdown;
    private boolean afterPeriod = false;

    public ActionCountdown(Workout pWorkout, Exercise pExercise, long pActionTime, long pRestTime,
                           long pAfterTime) {
        super(pWorkout, R.drawable.action, R.raw.action_beep, pActionTime);
        workout = pWorkout;
        exercise = pExercise;
        restCountdown = new RestCountdown(pWorkout, this, pRestTime);
        afterCountdown = new AfterCountdown(pWorkout, pAfterTime);
    }

    @Override
    public void afterFinished() {
        // Remove one exercise of the workout
        int exerciseNb = exercise.decreaseExerciseNb();
        final TextView setNbField = (TextView) workout.findViewById(R.id.exercise_nb);
        setNbField.setText(String.valueOf(exerciseNb));
        if (exerciseNb == 0) {
            if (workout.hasNext()) {
                afterPeriod = true;
                afterCountdown.startCountdown();
                // Display the next exercise
                try {
                    workout.getNext().display();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Display the application logo
                final TextView nameField = (TextView) workout.findViewById(R.id.exercise_name);
                nameField.setText("Workout Completed!");
                ImageView setImage = (ImageView) workout.findViewById(R.id.exercise_img);
                setImage.setImageResource(R.drawable.completed);
            }
        } else {
            restCountdown.startCountdown();
        }
    }

    public void stop() {
        cancel();
        restCountdown.cancel();
        afterCountdown.cancel();
        if(afterPeriod){

        }
    }
}
