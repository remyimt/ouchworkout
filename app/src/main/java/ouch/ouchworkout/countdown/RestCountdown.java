package ouch.ouchworkout.countdown;

import android.app.Activity;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;

public class RestCountdown extends AbstractCountdown {

    public RestCountdown(Activity pAct, long pTime) {
        super(pAct, pTime, R.drawable.rest, R.raw.sound_rest);
    }

    @Override
    public void onFinish() {
        countdownField.setText("000");
        Factory.getInstance().getCurrentWorkout().startExercise(activity);
    }
}
