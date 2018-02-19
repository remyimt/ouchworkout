package ouch.ouchworkout.countdown;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import ouch.ouchworkout.OuchSettings;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public abstract class AbstractCountdown extends CountDownTimer {
    protected final TextView countdownField;
    private final MediaPlayer mp;
    private final ImageView actionLight;
    private final int imageId;
    private boolean beepDone = false;


    public AbstractCountdown(long pTime, int pImageId, int pBeepId){
        super(pTime * 1000, 500);
        Workout w = Workout.getWorkout();
        countdownField = (TextView) w.findViewById(R.id.countdown);
        mp = MediaPlayer.create(w.getApplicationContext(), pBeepId);
        imageId = pImageId;
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

    public void startCountdown() {
        beepDone = false;
        actionLight.setImageResource(imageId);
        start();
    }
}
