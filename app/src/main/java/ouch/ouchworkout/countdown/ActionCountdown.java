package ouch.ouchworkout.countdown;

import android.app.Activity;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;

public class ActionCountdown extends AbstractCountdown {

    public ActionCountdown(Activity pAct, long pTime) {
        super(pAct, pTime, R.drawable.action, R.raw.sound_action);
    }

    @Override
    public void onFinish() {
        countdownField.setText("000");
        Factory.getInstance().getCurrentWorkout().endActionPhase(activity);
    }
}
