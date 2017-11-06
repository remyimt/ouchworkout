package ouch.ouchworkout;

import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

public class Set {
    private final String name;
    private final Workout workout;
    private final int repNb;
    private int setNb;
    private CountDownTimer actionTimer, restTimer;

    public Set(Workout pWorkout, String pSetName, int pSetNb, int pRepNb, int pActionTime, int pRestTime) {
        workout = pWorkout;
        name = pSetName;
        setNb = pSetNb;
        repNb = pRepNb;
        restTimer = new CountDownTimer(pRestTime * 1000, 500) {
            final TextView countdownField = (TextView) workout.findViewById(R.id.countdown);

            @Override
            public void onTick(long l) {
                countdownField.setText(String.valueOf(Math.round(l * 0.001f)));
            }

            @Override
            public void onFinish() {
                countdownField.setText("0");
                try {
                    if (setNb > 1) {
                        setNb--;
                        display();
                    } else {
                        workout.play();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        actionTimer = new CountDownTimer(pActionTime * 1000, 500) {
            final TextView countdownField = (TextView) workout.findViewById(R.id.countdown);
            final ImageView actionLight = (ImageView) workout.findViewById(R.id.action_light);

            public void onTick(long millisUntilFinished) {
                // Set the action time
                countdownField.setText(String.valueOf(Math.round(millisUntilFinished * 0.001f)));
            }

            public void onFinish() {
                actionLight.setImageResource(R.drawable.rest);
                restTimer.start();
            }
        };
    }

    public void display() {
        // Set the name of the set
        final TextView nameField = (TextView) workout.findViewById(R.id.set_name);
        nameField.setText(name);
        ImageView setImage = (ImageView) workout.findViewById(R.id.set_img);
        setImage.setImageResource(workout.findDrawableByName(name));
        // Set the number of sets
        final TextView setNbField = (TextView) workout.findViewById(R.id.set_nb);
        setNbField.setText(String.valueOf(setNb));
        // Set the number of reps
        final TextView repNbField = (TextView) workout.findViewById(R.id.rep_nb);
        repNbField.setText(String.valueOf(repNb));
        // Set the countdown
        ImageView actionLight = (ImageView) workout.findViewById(R.id.action_light);
        actionLight.setImageResource(R.drawable.action);
        actionTimer.start();
    }

    public void stop() {
        actionTimer.cancel();
        restTimer.cancel();
    }
}
