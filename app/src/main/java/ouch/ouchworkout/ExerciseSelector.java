package ouch.ouchworkout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExerciseSelector {
    private final List<Exercise> exercises = new ArrayList<>();
    private final List<Exercise> completed = new ArrayList<>();
    private Exercise current = null;
    private int workoutLengthSeconds = 0, workoutExerciseNb = 0;

    public ExerciseSelector() {
    }

    public int getWorkoutExerciseNb() {
        return workoutExerciseNb;
    }

    public int getWorkoutLengthSeconds() {
        return workoutLengthSeconds;
    }

    public Exercise getCurrentExercise() {
        return current;
    }

    public Exercise getLastCompletedExercise() {
        return completed.get(completed.size() - 1);
    }

    public int completedExerciseNb() {
        return completed.size();
    }

    public void addExercise(Exercise pEx) {
        exercises.add(pEx);
        workoutLengthSeconds += pEx.getLengthSeconds();
        workoutExerciseNb++;
    }

    public void setCurrentExerciseFromName(String pName) {
        current = null;
        for (Exercise exe : exercises) {
            if (exe.getName().equals(pName)) {
                current = exe;
            }
        }
    }

    public void removeExerciseFromNames(List<String> pNames) {
        Iterator<Exercise> it = exercises.iterator();
        while (it.hasNext()) {
            Exercise ex = it.next();
            if (pNames.contains(ex.getName())) {
                it.remove();
                workoutExerciseNb--;
                workoutLengthSeconds -= ex.getLengthSeconds();
            }
        }
    }

    public List<String> getExerciseNames() {
        List<String> names = new LinkedList<>();
        for (Exercise ex : exercises) {
            names.add(ex.getName());
        }
        return names;
    }

    public boolean completeExercise(Exercise pEx) {
        boolean isRemoved = exercises.remove(pEx);
        if (isRemoved) {
            completed.add(pEx);
        }
        return isRemoved;
    }

    public void selectNextExercise() {
        current = null;
        if (!exercises.isEmpty()) {
            current = exercises.get(0);
        }
    }

    public String dumpExercises() {
        List<Exercise> exeList = completed;
        if (exeList.isEmpty()) {
            exeList = exercises;
        }
        StringBuilder json = new StringBuilder();
        json.append("  \"workout\": [\n");
        for (Exercise exe : exeList) {
            json.append(exe.toJSON() + ",\n");
        }
        json.setLength(json.length() - 2);
        json.append("\n  ]");
        return json.toString();
    }
}
