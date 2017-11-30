package ouch.ouchworkout.ouch.workout.countdown;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import ouch.ouchworkout.OuchSettings;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public abstract class Countdown extends CountDownTimer {
    private final Workout workout;
    private final MediaPlayer mp;
    private final TextView countdownField;
    private final ImageView actionLight;
    private final int imageId;
    private final boolean isActionCountdown;
    private boolean beepDone = false;

    public Countdown(Workout pWorkout, boolean pIsActionCountdown, int pImageId, int pBeepId,
                     long millisInFuture) {
        super(millisInFuture, 500);
        workout = pWorkout;
        imageId = pImageId;
        isActionCountdown = pIsActionCountdown;
        mp = MediaPlayer.create(workout.getApplicationContext(), pBeepId);
        countdownField = (TextView) workout.findViewById(R.id.countdown);
        actionLight = (ImageView) workout.findViewById(R.id.action_light);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int seconds = Math.round(millisUntilFinished * 0.001f);
        if (seconds < 10) {
            countdownField.setText("00" + String.valueOf(seconds));
        } else if (seconds < 100) {
            countdownField.setText("0" + String.valueOf(seconds));
        } else {
            countdownField.setText(String.valueOf(seconds));
        }
        if (seconds == OuchSettings.BEEP_TIME_SECONDS && !beepDone && OuchSettings.WITH_SOUND) {
            beepDone = true;
            mp.start();
        }
    }

    @Override
    public void onFinish() {
        countdownField.setText("000");
        if (isActionCountdown) {
            // Decrease the number of remaining exercises
            int exerciseNb = workout.getCurrentExercise().decreaseExerciseNb();
            if (exerciseNb == 0 && workout.hasNextExercise()) {
                workout.getNextExercise().display();
            } else {
                final TextView setNbField = (TextView) workout.findViewById(R.id.exercise_nb);
                setNbField.setText(String.valueOf(exerciseNb));
            }
        }
        workout.nextCountdown();
    }

    public void startCountdown() {
        beepDone = false;
        actionLight.setImageResource(imageId);
        start();
    }
}
