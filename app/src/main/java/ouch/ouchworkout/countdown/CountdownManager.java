package ouch.ouchworkout.countdown;

import android.widget.TextView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class CountdownManager {
    private final AfterCountdown newStartCountdown;
    private final ActionCountdown actionCd;
    private final RestCountdown restCd;
    private final AfterCountdown afterCd;
    private final Exercise exercise;
    private final TextView exerciseCounter;
    private Countdown inProgress;
    private boolean isRunning = false;
    private boolean actionPhase = false;

    public CountdownManager(ActionCountdown pAction, RestCountdown pRest,
                            AfterCountdown pAfter, Exercise pExercise) {
        actionCd = pAction;
        restCd = pRest;
        afterCd = pAfter;
        newStartCountdown = new AfterCountdown(10000);
        exercise = pExercise;
        exerciseCounter = (TextView) Workout.getWorkout().findViewById(R.id.exercise_counter);
    }

    public void startNewStartCountdown() {
        actionPhase = false;
        inProgress = newStartCountdown;
        isRunning = true;
        newStartCountdown.startCountdown();
    }

    public boolean next() {
        if (actionPhase) {
            actionPhase = false;
            if (exercise.getExerciseNb() > 0) {
                inProgress = restCd;
                isRunning = true;
                restCd.startCountdown();
                return true;
            } else if (Workout.getWorkout().hasNextExercise()) {
                Workout w = Workout.getWorkout();
                w.selectNextExercise();
                int exerciseNb = w.getRemainingExerciseNumber();
                if (exerciseNb < 10) {
                    exerciseCounter.setText("0" + String.valueOf(exerciseNb));
                } else {
                    exerciseCounter.setText(String.valueOf(exerciseNb));
                }
                inProgress = afterCd;
                isRunning = true;
                afterCd.startCountdown();
                return true;
            } else {
                return false;
            }
        } else {
            if (exercise.getExerciseNb() > 0) {
                actionPhase = true;
                inProgress = actionCd;
                isRunning = true;
                actionCd.startCountdown();
                return true;
            } else {
                return false;
            }
        }
    }

    public void stop() {
        isRunning = false;
        inProgress.cancel();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
