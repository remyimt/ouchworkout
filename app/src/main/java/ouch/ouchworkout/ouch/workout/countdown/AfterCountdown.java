package ouch.ouchworkout.ouch.workout.countdown;


import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class AfterCountdown extends Countdown {
    private final Workout workout;

    public AfterCountdown(Workout pWorkout, long millisInFuture) {
        super(pWorkout, R.drawable.rest, R.raw.rest_beep, millisInFuture);
        workout = pWorkout;
    }

    @Override
    public void afterFinished() {
        // Execute the next exercise
        workout.startTheExercise();
    }
}
