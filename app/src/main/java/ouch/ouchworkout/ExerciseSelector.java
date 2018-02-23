package ouch.ouchworkout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExerciseSelector {
    private final List<Exercise> exercises = new ArrayList<>();
    private final List<Exercise> completed = new ArrayList<>();
    private Exercise current = null;
    private int workoutLengthSeconds = 0;

    public ExerciseSelector() {
    }

    public int getWorkoutLengthSeconds() {
        return workoutLengthSeconds;
    }

    public Exercise getCurrentExercise() {
        return current;
    }

    public int workoutExerciseNb() {
        return exercises.size();
    }

    public int completedExerciseNb() {
        return completed.size();
    }

    public void addExercise(Exercise pEx) {
        exercises.add(pEx);
        workoutLengthSeconds += pEx.getLengthSeconds();
    }

    public boolean completeExercise(Exercise pEx) {
        boolean isRemoved = exercises.remove(pEx);
        if (isRemoved) {
            completed.add(pEx);
        }
        return isRemoved;
    }

    public boolean selectNextExercise() {
        if (exercises.isEmpty()) {
            return false;
        } else {
            current = exercises.get(0);
            return true;
        }
    }

    public List<String> getExerciseNames() {
        List<String> names = new LinkedList<>();
        for (Exercise ex : exercises) {
            names.add(ex.getName());
        }
        return names;
    }

    public void removeExerciseFromNames(List<String> pNames) {
        Iterator<Exercise> it = exercises.iterator();
        while (it.hasNext()) {
            Exercise ex = it.next();
            if (pNames.contains(ex.getName())) {
                it.remove();
                workoutLengthSeconds -= ex.getLengthSeconds();
            }
        }
    }
}
