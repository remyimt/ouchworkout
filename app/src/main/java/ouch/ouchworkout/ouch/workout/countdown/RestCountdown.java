package ouch.ouchworkout.ouch.workout.countdown;


import ouch.ouchworkout.R;

public class RestCountdown extends Countdown {

    public RestCountdown(long pRestTime) {
        super(false, R.drawable.rest, R.raw.rest_beep, pRestTime);
    }
}
