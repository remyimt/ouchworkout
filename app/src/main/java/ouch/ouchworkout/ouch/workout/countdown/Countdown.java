package ouch.ouchworkout.ouch.workout.countdown;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import ouch.ouchworkout.OuchSettings;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public abstract class Countdown extends CountDownTimer {
    private final MediaPlayer mp;
    private final TextView countdownField;
    private final ImageView actionLight;
    private final int imageId;
    private final boolean isActionCountdown;
    private boolean beepDone = false;

    public Countdown(boolean pIsActionCountdown, int pImageId, int pBeepId,
                     long millisInFuture) {
        super(millisInFuture, 500);
        imageId = pImageId;
        isActionCountdown = pIsActionCountdown;
        Workout w = Workout.getWorkout();
        mp = MediaPlayer.create(w.getApplicationContext(), pBeepId);
        countdownField = (TextView) w.findViewById(R.id.countdown);
        actionLight = (ImageView) w.findViewById(R.id.action_light);
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
        Workout w = Workout.getWorkout();
        if (isActionCountdown) {
            // Decrease the number of remaining exercises
            int exerciseNb = w.getCurrentExercise().decreaseExerciseNb();
            if (exerciseNb == 0 && w.hasNextExercise()) {
                w.getNextExercise().display();
            } else {
                final TextView setNbField = (TextView) w.findViewById(R.id.exercise_nb);
                setNbField.setText(String.valueOf(exerciseNb));
            }
        }
        w.nextCountdown();
    }

    public void startCountdown() {
        beepDone = false;
        actionLight.setImageResource(imageId);
        start();
    }
}
