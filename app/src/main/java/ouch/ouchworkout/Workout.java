package ouch.ouchworkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ouch.ouchworkout.activities.AfterExerciseAct;
import ouch.ouchworkout.activities.CompletedWorkoutAct;
import ouch.ouchworkout.activities.ExecutingExerciseAct;
import ouch.ouchworkout.countdown.AbstractCountdown;
import ouch.ouchworkout.countdown.ActionCountdown;
import ouch.ouchworkout.countdown.ResumeCountdown;
import ouch.ouchworkout.countdown.RestCountdown;

public class Workout {
    private static Workout workout = null;
    private final String filename, name;
    private final ExerciseSelector selector;
    private final int resumeTime = 10;
    private Activity activity;
    private boolean actionPhase, running, modified, incomplete;
    private AbstractCountdown resumeCd, restCd, actionCd;

    private Workout(String pFile, String pName, JSONArray pDesc) throws JSONException {
        name = pName;
        filename = pFile;
        selector = new ExerciseSelector();
        actionPhase = false;
        running = false;
        modified = false;
        incomplete = false;
        for (int i = 0; i < pDesc.length(); i++) {
            JSONObject info = pDesc.getJSONObject(i);
            Exercise exe = new Exercise(this, info.getString("name"),
                    info.getString("img"), info.getInt("set_nb"),
                    info.getInt("rep_nb"), info.getInt("load_kg"),
                    info.getInt("action_sec"), info.getInt("rest_sec"),
                    info.getInt("after_sec"));
            selector.addExercise(exe);
        }
    }

    public static Workout createWorkout(String pFilename, String pName, JSONArray pDesc) throws JSONException {
        workout = new Workout(pFilename, pName, pDesc);
        return workout;
    }

    public static Workout getWorkout() {
        return workout;
    }

    public static boolean hasWorkout() {
        return workout != null;
    }

    public String getFilename() {
        return filename;
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

    public String toJSON() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"name\": \"" + name + "\",\n");
        json.append(selector.dumpExercises() + "\n");
        json.append("}\n");
        return json.toString();
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
        Button done = (Button) activity.findViewById(R.id.done_button);
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
            ImageView actionLight = (ImageView) activity.findViewById(R.id.action_light);
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
            if (selector.selectNextExercise()) {
                Intent intent = new Intent(activity.getApplicationContext(), AfterExerciseAct.class);
                activity.startActivity(intent);
            } else {
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

    public boolean selectNextExercise() {
        return selector.selectNextExercise();
    }

    public boolean isWorkoutStarted() {
        return selector.completedExerciseNb() != 0 || selector.getCurrentExercise().isStarted();
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
}
