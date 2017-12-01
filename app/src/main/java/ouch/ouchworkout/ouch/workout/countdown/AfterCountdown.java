package ouch.ouchworkout.ouch.workout.countdown;


import ouch.ouchworkout.R;

public class AfterCountdown extends Countdown {
    public AfterCountdown(long pAfterTime) {
        super(false, R.drawable.rest, R.raw.rest_beep, pAfterTime);
    }
}
