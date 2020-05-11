package ouch.ouchworkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ouch.ouchworkout.activities.SAfterExercise;
import ouch.ouchworkout.activities.SCompletedWorkout;
import ouch.ouchworkout.activities.SExecutingExercise;
import ouch.ouchworkout.activities.SExerciseManualSelection;
import ouch.ouchworkout.countdown.AbstractCountdown;
import ouch.ouchworkout.countdown.ActionCountdown;
import ouch.ouchworkout.countdown.RestCountdown;
import ouch.ouchworkout.countdown.ResumeCountdown;
import ouch.ouchworkout.exception.NoExerciseException;

public class Workout {
    private String name;
    private final List<Exercise> exercises;
    private List<Exercise> runningExercises;
    private Exercise previousExercise = null;
    private final int resumeTime = 10;
    private int workoutLengthSeconds = 0, currentExerciseIdx = 0, nbCompletedExercises = 0;
    private boolean actionPhase, running;
    private AbstractCountdown resumeCd, restCd, actionCd;

    protected Workout(String pName) {
        name = pName;
        exercises = new ArrayList<>();
        runningExercises = new ArrayList<>();
        actionPhase = false;
        running = false;
    }

    protected Workout(JSONObject pDesc) throws JSONException {
        name = pDesc.getString("name");
        exercises = new ArrayList<>();
        runningExercises = new ArrayList<>();
        JSONArray exDesc = pDesc.getJSONArray("exercises");
        for (int i = 0; i < exDesc.length(); i++) {
            JSONObject info = exDesc.getJSONObject(i);
            exercises.add(new Exercise(info));
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getResumeTime() {
        return resumeTime;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isActionPhase() {
        return actionPhase;
    }

    // Do not edit the exercise list from this method
    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    // Do not edit the running exercise list from this method
    public List<Exercise> getRunningExercises() {
        return new ArrayList<>(runningExercises);
    }

    public Exercise getExerciseFromName(String pName) throws NoExerciseException {
        for (Exercise ex : exercises) {
            if (ex.getName().equals(pName)) {
                return ex;
            }
        }
        throw new NoExerciseException();
    }

    public boolean isWorkoutStarted() {
        return nbCompletedExercises != 0 || getCurrentRunningExercise().isStarted();
    }

    public boolean isWorkoutCompleted() {
        return runningExercises.size() - nbCompletedExercises == 0;
    }

    public int getLengthMinutes() {
        return workoutLengthSeconds / 60;
    }

    public int getLengthSeconds() {
        return workoutLengthSeconds % 60;
    }

    // Iterate over exercises while editing exercises
    public Exercise getCurrentExercise() {
        return exercises.get(currentExerciseIdx);
    }

    public Exercise getFirstExercise() {
        currentExerciseIdx = 0;
        return getCurrentExercise();
    }

    public Exercise getNextExercise() {
        currentExerciseIdx++;
        return getCurrentExercise();
    }

    // Iterate over exercises while running workouts
    public Exercise getPreviousRunningExercise() {
        return previousExercise;
    }

    public void setPreviousRunningExercise(Exercise pCurrent) {
        previousExercise = pCurrent;
    }

    public boolean selectNextRunningExercise() {
        if (isLastExercise()) {
            return false;
        } else {
            currentExerciseIdx++;
            return true;
        }
    }

    public Exercise getCurrentRunningExercise() {
        return runningExercises.get(currentExerciseIdx);
    }

    // Workout file operations
    public void saveJSON(Activity pAct) {
        try {
            // Build the JSON data
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"name\": \"" + name + "\",\n");
            json.append("  \"exercises\": [\n");
            for (Exercise ex : exercises) {
                json.append(ex.toJSON().append(",\n"));
            }
            // Remove the ending coma and new line
            json.setLength(json.length() - 2);
            json.append("\n  ]\n");
            json.append("}\n");
            // Write the JSON file
            File external = new File(Factory.getSettings().getExternalDirectory(pAct),
                    name.replaceAll("/[^A-Za-z0-9]/", "_") + ".json");
            FileOutputStream output = new FileOutputStream(external);
            PrintWriter writer = new PrintWriter(output);
            writer.print(json);
            writer.flush();
            writer.close();
            output.close();
            // Save the workout in the factory (only for new workouts)
            Factory.getInstance().saveCurrentWorkout();
            // Try to add the file to the media scanner
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(external));
            pAct.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteJSON(Activity pAct) {
        try {
            File external = new File(Factory.getSettings().getExternalDirectory(pAct),
                    name.replaceAll("/[^A-Za-z0-9]/", "_") + ".json");
            external.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Edit workouts
    public void setName(String pName) {
        name = pName;
    }

    public void addExercise(Exercise pEx) {
        exercises.add(pEx);
    }

    public boolean removeExercise(Exercise pEx) {
        return exercises.remove(pEx);
    }

    public boolean moveUpExercise(Exercise pEx) {
        int index = exercises.indexOf(pEx);
        if (index > 0) {
            exercises.remove(pEx);
            exercises.add(index - 1, pEx);
            return true;
        } else {
            return false;
        }
    }

    public boolean moveDownExercise(Exercise pEx) {
        int index = exercises.indexOf(pEx);
        if (index < exercises.size() - 1) {
            exercises.remove(pEx);
            exercises.add(index + 1, pEx);
            return true;
        } else {
            return false;
        }
    }

    public void setRunningExercises(List<Exercise> pToRemove) {
        runningExercises = new ArrayList<>(exercises);
        runningExercises.removeAll(pToRemove);
        // Compute the length of the workout
        workoutLengthSeconds = 0;
        for (Exercise ex : runningExercises) {
            workoutLengthSeconds += ex.getLengthSeconds();
        }
    }

    // Running workouts
    public void initialize() {
        nbCompletedExercises = 0;
        currentExerciseIdx = 0;
        previousExercise = null;
        for (Exercise ex : runningExercises) {
            ex.initialize();
        }
    }

    public void setRunningExercise(Exercise ex) {
        currentExerciseIdx = runningExercises.indexOf(ex);
    }

    public boolean containsExercise(Exercise pEx) {
        return exercises.contains(pEx);
    }

    public boolean isLastExercise() {
        return currentExerciseIdx + 1 == exercises.size();
    }

    // Display exercises
    public void updateProgressBar(ProgressBar pBar) {
        // Set the position of the progress bar
        pBar.setMax(runningExercises.size());
        pBar.setProgress(nbCompletedExercises);
    }

    public void loadExercise(final Activity pAct) {
        // Configure the exercise
        Exercise ex = getCurrentRunningExercise();
        // Configure the done button (used if actionTime == 0)
        Button done = pAct.findViewById(R.id.done_button);
        if (ex.isDoneButtonRequired()) {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (actionPhase) {
                        endActionPhase(pAct);
                    }
                }
            });
        }
        done.setVisibility(View.INVISIBLE);
        // Configure countdowns for the exercise
        actionCd = new ActionCountdown(pAct, ex.getActionTime());
        restCd = new RestCountdown(pAct, ex.getRestTime());
        resumeCd = new ResumeCountdown(pAct, resumeTime);
    }

    public void resumeWorkout(Activity pAct) {
        running = false;
        Intent intent = new Intent(pAct, SExecutingExercise.class);
        pAct.startActivity(intent);
    }

    public void startWorkout(Context pCont) {
        running = true;
        Intent intent = new Intent(pCont, SExecutingExercise.class);
        pCont.startActivity(intent);
    }

    public void startExercise(Activity pAct) {
        actionPhase = true;
        if (getCurrentRunningExercise().isDoneButtonRequired()) {
            ImageView actionLight = pAct.findViewById(R.id.action_light);
            actionLight.setImageResource(R.drawable.action);
            pAct.findViewById(R.id.done_button).setVisibility(View.VISIBLE);
        } else {
            actionCd.startCountdown();
        }
    }

    public void endActionPhase(Activity pAct) {
        actionPhase = false;
        Exercise exe = getCurrentRunningExercise();
        if (exe.isDoneButtonRequired()) {
            pAct.findViewById(R.id.done_button).setVisibility(View.INVISIBLE);
        }
        exe.decreaseSetNb(pAct);
        if (exe.getCurrentSetNb() > 0) {
            // Continue the same exercise
            restCd.startCountdown();
        } else {
            // The exercise is completed
            nbCompletedExercises++;
            // Start the next exercise
            setPreviousRunningExercise(exe);
            if (isWorkoutCompleted()) {
                // No more exercise
                Intent intent = new Intent(pAct.getApplicationContext(), SCompletedWorkout.class);
                pAct.startActivity(intent);
            } else {
                // There are other exercises
                if (Factory.getSettings().isManualSelection()) {
                    // Manually select the first exercise of the workout
                    Intent intent = new Intent(pAct.getApplicationContext(), SExerciseManualSelection.class);
                    pAct.startActivity(intent);
                } else {
                    selectNextRunningExercise();
                    displayCurrentExercise(pAct);
                }
            }
        }
    }

    public void displayCurrentExercise(Activity pAct) {
        Intent intent = new Intent(pAct.getApplicationContext(), SAfterExercise.class);
        pAct.startActivity(intent);
    }

    // Event fired by the playPause button
    public void playPause() {
        if (running) {
            actionPhase = false;
            running = false;
            actionCd.cancel();
            restCd.cancel();
            resumeCd.cancel();
        } else {
            running = true;
            resumeCd.startCountdown();
        }
    }
}
