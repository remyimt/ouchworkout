package ouch.ouchworkout.countdown;


import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;

public class ActionCountdown extends Countdown {

    public ActionCountdown( Exercise pExercise) {
        super(true, R.drawable.action, R.raw.action_beep,
                pExercise.getActionTime() * 1000);
    }
}
