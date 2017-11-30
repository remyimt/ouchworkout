package ouch.ouchworkout.ouch.workout.countdown;


import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class AfterCountdown extends Countdown {
    public AfterCountdown(Workout pWorkout, long pAfterTime) {
        super(pWorkout, false, R.drawable.rest, R.raw.rest_beep, pAfterTime);
    }
}
