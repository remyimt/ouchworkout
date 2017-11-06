package ouch.ouchworkout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Workout {
    private final JSONArray description;
    private final Activity activity;
    private int setIndex = 0;
    private Set currentSet;
    private boolean isRunning =  false;

    public Workout(Activity pAct, JSONArray pDesc) {
        activity = pAct;
        description = pDesc;
    }

    public void play() throws JSONException {
        isRunning = true;
        if (description.length() > setIndex) {
            JSONObject info = description.getJSONObject(setIndex);
            setIndex++;
            currentSet = new Set(this, info.getString("name"), info.getInt("nb_set"),
                    info.getInt("nb_rep"), info.getInt("action"),
                    info.getInt("rest"));
            currentSet.display();
        } else {
            // End of the workout
        }
    }

    public void stop() {
        isRunning = false;
        currentSet.stop();
    }

    public boolean isRunning(){
        return isRunning;
    }

    // Application Functions
    public Context getApplicationContext() {
        return activity.getApplicationContext();
    }

    public View findViewById(int pId) {
        return activity.findViewById(pId);
    }

    public int findDrawableByName(String pFilename){
        return activity.getResources().getIdentifier(pFilename, "drawable", activity.getPackageName());
    }
}
