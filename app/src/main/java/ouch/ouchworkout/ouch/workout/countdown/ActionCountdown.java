package ouch.ouchworkout.ouch.workout.countdown;


import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class ActionCountdown extends Countdown {

    public ActionCountdown(Workout pWorkout, Exercise pExercise) {
        super(pWorkout, true, R.drawable.action, R.raw.action_beep,
                pExercise.getActionTime() * 1000);
    }
}
