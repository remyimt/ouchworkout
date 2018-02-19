package ouch.ouchworkout.countdown;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class ActionCountdown extends AbstractCountdown {

    public ActionCountdown(long pTime) {
        super(pTime, R.drawable.action, R.raw.action_beep);
    }

    @Override
    public void onFinish() {
        Workout w = Workout.getWorkout();
        w.decreaseActionNb();
        countdownField.setText("000");
        Workout.getWorkout().next();
    }
}
