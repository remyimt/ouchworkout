package ouch.ouchworkout;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Exercise {
    private final String name, pictureName, type, target;
    private int restTime, recoverTime, lengthSeconds, difficulty;
    private int actionTime, setNb, repNb, loadKg, currentSetNb;

    public Exercise(JSONObject pJson) throws JSONException {
        name = pJson.getString("name");
        type = pJson.getString("type");
        target = pJson.getString("target");
        difficulty = pJson.getInt("difficulty");
        actionTime = pJson.getInt("action_sec");
        restTime = pJson.getInt("rest_sec");
        recoverTime = pJson.optInt("recover_sec", 30);
        setNb = pJson.getInt("set_nb");
        currentSetNb = setNb;
        repNb = pJson.getInt("rep_nb");
        loadKg = pJson.getInt("load_kg");
        lengthSeconds = setNb * actionTime + (setNb - 1) * restTime + recoverTime;
        pictureName = pJson.getString("img");
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPictureName() {
        return pictureName;
    }

    public int getSetNb() {
        return setNb;
    }

    public int getRepNb() {
        return repNb;
    }

    public int getLoadKg() {
        return loadKg;
    }

    public int getCurrentSetNb() {
        return currentSetNb;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
    }

    public int getActionTime() {
        return actionTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getRecoverTime() {
        return recoverTime;
    }

    public boolean isDoneButtonRequired() {
        return actionTime == 0;
    }

    public boolean isStarted() {
        return setNb != currentSetNb;
    }

    public boolean isCompleted() {
        return currentSetNb == 0;
    }

    // Setters
    public void setSetNb(int pNb) {
        setNb = pNb;
    }

    public void setRepNb(int pNb) {
        repNb = pNb;
    }

    public void setLoadKg(int pKg) {
        loadKg = pKg;
    }

    public void setActionTime(int pTime) {
        actionTime = pTime;
    }

    public void setRestTime(int pTime) {
        restTime = pTime;
    }

    public void setRecoverTime(int pTime) {
        recoverTime = pTime;
    }

    // Initialize the exercise
    public void initialize() {
        currentSetNb = setNb;
    }

    // Display the exercise during a running workout
    public void display(Activity pAct, int pNameId, int pSetNbId, int pRepNbId,
                        int pImageId, int pLoadContainerId) {
        // Display the name of the exercise
        TextView nameField = pAct.findViewById(pNameId);
        nameField.setText(name);
        // Display the image of the exercise
        ImageView setImage = pAct.findViewById(pImageId);
        setImage.setImageResource(pAct.getResources().getIdentifier(
                pictureName, "drawable", pAct.getPackageName()));
        // Display the number of sets
        TextView setNbField = pAct.findViewById(pSetNbId);
        setNbField.setText(String.valueOf(currentSetNb));
        // Display the number of reps
        TextView repNbField = pAct.findViewById(pRepNbId);
        repNbField.setText(String.valueOf(repNb));
        // Display the load of the exercise
        LinearLayout loadContainer = pAct.findViewById(pLoadContainerId);
        if (loadKg > 0) {
            loadContainer.setVisibility(View.VISIBLE);
            ((TextView) loadContainer.getChildAt(1)).setText(String.valueOf(loadKg));
        } else {
            loadContainer.setVisibility(View.INVISIBLE);
        }
    }

    // Property modifiers
    public void decreaseSetNb(Activity pAct) {
        currentSetNb--;
        // Display the number of sets
        TextView setNbField = pAct.findViewById(R.id.set_nb);
        setNbField.setText(String.valueOf(currentSetNb));
    }

    @Override
    public boolean equals(Object obj) {
        Exercise other = (Exercise) obj;
        return other.getName().equals(name) && other.getPictureName().equals(pictureName);
    }

    public StringBuilder toJSON() {
        StringBuilder json = new StringBuilder();
        String spaces = "    ";
        json.append(spaces + "{\n");
        json.append(spaces + "  \"name\": \"" + name + "\",\n");
        json.append(spaces + "  \"type\": \"" + type + "\",\n");
        json.append(spaces + "  \"target\": \"" + target + "\",\n");
        json.append(spaces + "  \"difficulty\": \"" + difficulty + "\",\n");
        json.append(spaces + "  \"img\": \"" + pictureName + "\",\n");
        json.append(spaces + "  \"set_nb\":" + setNb + ",\n");
        json.append(spaces + "  \"rep_nb\":" + repNb + ",\n");
        json.append(spaces + "  \"load_kg\":" + loadKg + ",\n");
        json.append(spaces + "  \"action_sec\":" + actionTime + ",\n");
        json.append(spaces + "  \"rest_sec\":" + restTime + ",\n");
        json.append(spaces + "  \"recover_sec\":" + recoverTime + "\n");
        json.append(spaces + "}");
        return json;
    }
}