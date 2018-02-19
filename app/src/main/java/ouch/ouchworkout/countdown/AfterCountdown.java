package ouch.ouchworkout.countdown;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class AfterCountdown extends AbstractCountdown {
    public AfterCountdown(long pTime) {
        super(pTime, R.drawable.rest, R.raw.rest_beep);
    }

    @Override
    public void onFinish() {
        countdownField.setText("000");
        Workout.getWorkout().next();
    }
}
