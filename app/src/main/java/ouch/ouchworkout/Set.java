package ouch.ouchworkout;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

public class Set {
    private final long LAST_BEEP_VALUE = 2000L;
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
            private final MediaPlayer mp = MediaPlayer.create(workout.getApplicationContext(), R.raw.rest_beep);
            final TextView countdownField = (TextView) workout.findViewById(R.id.countdown);
            private long lastBeep = LAST_BEEP_VALUE;

            @Override
            public void onTick(long millisUntilFinished) {
                countdownField.setText(String.valueOf(Math.round(millisUntilFinished * 0.001f)));
                if (millisUntilFinished < lastBeep) {
                    mp.start();
                    if(lastBeep > 1000){
                        lastBeep -= 1000L;
                    } else {
                        lastBeep = LAST_BEEP_VALUE;
                    }
                }
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
            private final MediaPlayer mp = MediaPlayer.create(workout.getApplicationContext(), R.raw.action_beep);
            private final TextView countdownField = (TextView) workout.findViewById(R.id.countdown);
            private final ImageView actionLight = (ImageView) workout.findViewById(R.id.action_light);
            private long lastBeep = LAST_BEEP_VALUE;

            public void onTick(long millisUntilFinished) {
                // Set the action time
                countdownField.setText(String.valueOf(Math.round(millisUntilFinished * 0.001f)));
                if (millisUntilFinished < lastBeep) {
                    mp.start();
                    if(lastBeep > 1000){
                        lastBeep -= 1000L;
                    } else {
                        lastBeep = LAST_BEEP_VALUE;
                    }
                }
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
