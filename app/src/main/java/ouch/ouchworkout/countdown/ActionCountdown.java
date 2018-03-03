package ouch.ouchworkout.countdown;

import android.app.Activity;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class ActionCountdown extends AbstractCountdown {

    public ActionCountdown(Activity pAct, long pTime) {
        super(pAct, pTime, R.drawable.action, R.raw.action_beep);
    }

    @Override
    public void onFinish() {
        countdownField.setText("000");
        Workout.getWorkout().endActionPhase();
    }
}
