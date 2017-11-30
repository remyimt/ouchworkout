package ouch.ouchworkout.ouch.workout.countdown;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Workout;

public class CountdownManager {
    private final Workout workout;
    private final AfterCountdown newStartCountdown;
    private final ActionCountdown actionCd;
    private final RestCountdown restCd;
    private final AfterCountdown afterCd;
    private final Exercise exercise;
    private Countdown inProgress;
    private boolean isRunning = false;
    private boolean actionPhase = false;

    public CountdownManager(Workout pWorkout, ActionCountdown pAction, RestCountdown pRest,
                            AfterCountdown pAfter, Exercise pExercise) {
        workout = pWorkout;
        actionCd = pAction;
        restCd = pRest;
        afterCd = pAfter;
        newStartCountdown = new AfterCountdown(pWorkout, 10000);
        exercise = pExercise;
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
            } else if (workout.hasNextExercise()) {
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
