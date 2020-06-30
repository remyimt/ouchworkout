package ouch.ouchworkout.countdown;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;

public abstract class AbstractCountdown extends CountDownTimer {
    protected final TextView countdownField;
    private final ImageView actionLight;
    private final int imageId, beepId;
    protected final Activity activity;
    private boolean beepDone = false;


    public AbstractCountdown(Activity pAct, long pTime, int pImageId, int pBeepId) {
        super(pTime * 1000, 500);
        activity = pAct;
        countdownField = pAct.findViewById(R.id.countdown);
        beepId = pBeepId;
        imageId = pImageId;
        actionLight = pAct.findViewById(R.id.action_light);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int seconds = Math.round(millisUntilFinished * 0.001f);
        countdownField.setText(formatCountdown(seconds));
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
            MediaPlayer mp = MediaPlayer.create(activity.getApplicationContext(), beepId);
            mp.start();
        }
    }

    public void startCountdown() {
        beepDone = false;
        actionLight.setImageResource(imageId);
        start();
    }

    public static String formatCountdown(int pSeconds) {
        if (pSeconds < 10) {
            return "00" + String.valueOf(pSeconds);
        } else if (pSeconds < 100) {
            return "0" + String.valueOf(pSeconds);
        } else {
            return String.valueOf(pSeconds);
        }
    }
}
