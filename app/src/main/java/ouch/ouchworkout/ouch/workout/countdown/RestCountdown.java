package ouch.ouchworkout.ouch.workout.countdown;


import android.widget.ImageView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class RestCountdown extends Countdown {

    public RestCountdown(Workout pWorkout, long pRestTime) {
        super(pWorkout, false, R.drawable.rest, R.raw.rest_beep, pRestTime);
    }
}
