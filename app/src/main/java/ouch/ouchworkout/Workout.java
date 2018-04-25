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
import java.util.List;

import ouch.ouchworkout.activities.AfterExerciseAct;
import ouch.ouchworkout.activities.CompletedWorkoutAct;
import ouch.ouchworkout.activities.ExecutingExerciseAct;
import ouch.ouchworkout.activities.ExercisePickerAct;
import ouch.ouchworkout.countdown.AbstractCountdown;
import ouch.ouchworkout.countdown.ActionCountdown;
import ouch.ouchworkout.countdown.RestCountdown;
import ouch.ouchworkout.countdown.ResumeCountdown;

public class Workout {
    private static Workout workout = null;
    private final String filename, name;
    private final ExerciseSelector selector;
    private final int resumeTime = 10;
    private final int difficulty;
    private Activity activity;
    private boolean actionPhase, running, modified, incomplete;
    private AbstractCountdown resumeCd, restCd, actionCd;

    private Workout(String pFile, String pName, int pDifficulty, JSONArray pDesc) throws JSONException {
        name = pName;
        filename = pFile;
        difficulty = pDifficulty;
        selector = new ExerciseSelector();
        actionPhase = false;
        running = false;
        modified = false;
        incomplete = false;
        for (int i = 0; i < pDesc.length(); i++) {
            JSONObject info = pDesc.getJSONObject(i);
            Exercise exe = new Exercise(info.getString("name"), info.getString("img"),
                    info.getInt("set_nb"), info.getInt("rep_nb"),
                    info.getInt("load_kg"), info.getInt("action_sec"),
                    info.getInt("rest_sec"), info.getInt("after_sec"));
            selector.addExercise(exe);
        }
    }

    public static Workout createWorkout(String pFilename, String pName, int pDifficulty,
                                        JSONArray pDesc) throws JSONException {
        workout = new Workout(pFilename, pName, pDifficulty, pDesc);
        return workout;
    }

    public static Workout getWorkout() {
        return workout;
    }

    public static boolean hasWorkout() {
        return workout != null;
    }

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

    public boolean isIncomplete() {
        return incomplete;
    }

    public boolean isModified() {
        return modified;
    }

    public void modified() {
        modified = true;
    }

    public void saveJSON(Activity pAct) {
        try {
            // Build the JSON data
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"name\": \"" + name + "\",\n");
            json.append("  \"difficulty\": " + difficulty + ",\n");
            json.append(selector.dumpExercises() + "\n");
            json.append("}\n");
            // Write the JSON file
            File external = new File(Settings.getSettings().getExternalDirectory(pAct),
                    filename + ".json");
            FileOutputStream output = new FileOutputStream(external);
            PrintWriter writer = new PrintWriter(output);
            writer.print(json);
            writer.flush();
            writer.close();
            output.close();
            // Try to add the file to the media scanner
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(external));
            pAct.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar(ProgressBar pBar) {
        // Set the position of the progress bar
        pBar.setMax(selector.getWorkoutExerciseNb());
        pBar.setProgress(selector.completedExerciseNb());
    }

    public void loadExercise(Activity pAct) {
        activity = pAct;
        // Configure the exercise
        Exercise ex = selector.getCurrentExercise();
        // Configure the done button (used if actionTime == 0)
        Button done = activity.findViewById(R.id.done_button);
        if (ex.isDoneButtonRequired()) {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (actionPhase) {
                        endActionPhase();
                    }
                }
            });
        }
        done.setVisibility(View.INVISIBLE);
        // Configure countdowns for the exercise
        actionCd = new ActionCountdown(activity, ex.getActionTime());
        restCd = new RestCountdown(activity, ex.getRestTime());
        resumeCd = new ResumeCountdown(activity, resumeTime);
    }

    public void resumeWorkout(Activity pAct) {
        running = false;
        Intent intent = new Intent(pAct, ExecutingExerciseAct.class);
        pAct.startActivity(intent);
    }

    public void startWorkout(Context pCont) {
        running = true;
        Intent intent = new Intent(pCont, ExecutingExerciseAct.class);
        pCont.startActivity(intent);
    }

    public void startExercise() {
        actionPhase = true;
        if (getCurrentExercise().isDoneButtonRequired()) {
            ImageView actionLight = activity.findViewById(R.id.action_light);
            actionLight.setImageResource(R.drawable.action);
            activity.findViewById(R.id.done_button).setVisibility(View.VISIBLE);
        } else {
            actionCd.startCountdown();
        }
    }

    public void endActionPhase() {
        actionPhase = false;
        Exercise exe = selector.getCurrentExercise();
        if (exe.isDoneButtonRequired()) {
            activity.findViewById(R.id.done_button).setVisibility(View.INVISIBLE);
        }
        exe.decreaseSetNb(activity);
        if (exe.getCurrentSetNb() > 0) {
            // Continue the same exercise
            restCd.startCountdown();
        } else {
            // The exercise is completed
            selector.completeExercise(exe);
            // Start the next exercise
            if (selector.getExerciseNames().size() > 0) {
                // There are other exercises
                selectNextExercise();
            } else {
                // No more exercise
                Intent intent = new Intent(activity.getApplicationContext(), CompletedWorkoutAct.class);
                activity.startActivity(intent);
            }
        }
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

    // Selector forwarding methods
    public boolean isInProgress() {
        Exercise exe = selector.getCurrentExercise();
        return exe != null && exe.getCurrentSetNb() > 0;
    }

    public void selectNextExercise(Activity pAct) {
        if (Settings.getSettings().isManualSelection() && getExerciseNames().size() > 1) {
            Intent intent = new Intent(pAct.getApplicationContext(), ExercisePickerAct.class);
            pAct.startActivity(intent);
        } else {
            selector.selectNextExercise();
            if (running) {
                Intent intent = new Intent(pAct.getApplicationContext(), AfterExerciseAct.class);
                pAct.startActivity(intent);
            } else {
                resumeWorkout(pAct);
            }
        }
    }

    public void selectNextExercise() {
        selectNextExercise(activity);
    }

    public void setCurrentExerciseFromName(String pName) {
        selector.setCurrentExerciseFromName(pName);
    }

    public boolean isWorkoutStarted() {
        return selector.completedExerciseNb() != 0 ||
                (selector.getCurrentExercise() != null && selector.getCurrentExercise().isStarted());
    }


    public List<String> getExerciseNames() {
        return selector.getExerciseNames();
    }

    public void removeExerciseFromNames(List<String> pNames) {
        if (!pNames.isEmpty()) {
            incomplete = true;
            selector.removeExerciseFromNames(pNames);
        }
    }

    public Exercise getLastCompletedExercise() {
        return selector.getLastCompletedExercise();
    }

    public Exercise getCurrentExercise() {
        return selector.getCurrentExercise();
    }

    public int getLengthMinutes() {
        return selector.getWorkoutLengthSeconds() / 60;
    }

    public int getLengthSeconds() {
        return selector.getWorkoutLengthSeconds() % 60;
    }
}
