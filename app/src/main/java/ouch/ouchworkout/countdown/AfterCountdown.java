package ouch.ouchworkout.countdown;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;

public class AfterCountdown extends CountDownTimer {
    private final Activity activity;
    private final TextView countdownField;
    private final MediaPlayer mp;
    private boolean beepDone = false;


    public AfterCountdown(Activity pAct, long pTime) {
        super(pTime * 1000, 500);
        activity = pAct;
        countdownField = activity.findViewById(R.id.after_countdown);
        mp = MediaPlayer.create(activity.getApplicationContext(), R.raw.sound_rest);
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
        if (seconds == Factory.getSettings().getBeepTimeSeconds() && !beepDone
                && Factory.getSettings().isWithSound()) {
            beepDone = true;
            mp.start();
        }
    }

    @Override
    public void onFinish() {
        countdownField.setText("000");
        Factory.getInstance().getCurrentWorkout().startWorkout(activity);
    }
}
