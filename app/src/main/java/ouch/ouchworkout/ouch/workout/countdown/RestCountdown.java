package ouch.ouchworkout.ouch.workout.countdown;


import android.widget.ImageView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class RestCountdown extends Countdown {
    private Countdown actionCountdown;

    public RestCountdown(Workout pWorkout, Countdown pActionCountdown, long millisInFuture) {
        super(pWorkout, R.drawable.rest, R.raw.rest_beep, millisInFuture);
        actionCountdown = pActionCountdown;
    }

    @Override
    public void afterFinished() {
        // Start the action period
        actionCountdown.startCountdown();
    }
}
