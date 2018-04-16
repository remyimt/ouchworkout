package ouch.ouchworkout;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Exercise {
    private final String name;
    private final String pictureName;
    private final int restTime, afterTime, lengthSeconds;
    private int actionTime, setNb, repNb, loadKg, currentSetNb;

    public Exercise(String pExerciseName, String pImageName, int pSetNb,
                    int pRepNb, int pLoad, int pActionTime, int pRestTime, int pAfterTime) {
        name = pExerciseName;
        // If actionTime = 0 then add 'done' button to finish the set
        actionTime = pActionTime;
        restTime = pRestTime;
        afterTime = pAfterTime;
        currentSetNb = pSetNb;
        setNb = pSetNb;
        repNb = pRepNb;
        loadKg = pLoad;
        lengthSeconds = pSetNb * pActionTime + (pSetNb - 1) * pRestTime + pAfterTime;
        if (pImageName == null || pImageName.length() == 0) {
            pictureName = "";
        } else {
            pictureName = pImageName;
        }
    }

    public String getName() {
        return name;
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

    public int getAfterTime() {
        return afterTime;
    }

    public boolean isDoneButtonRequired() {
        return actionTime == 0;
    }

    public boolean isStarted() {
        return setNb != currentSetNb;
    }

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

    public void shortDisplay(Activity pAct, int pNameId, int pSetNbId, int pRepNbId,
                             int pLoadId, int pActionTimeId) {
        // Display the name of the exercise
        TextView nameField = pAct.findViewById(pNameId);
        nameField.setText(name);
        // Display the number of sets
        TextView setNbField = pAct.findViewById(pSetNbId);
        setNbField.setText(String.valueOf(setNb));
        // Display the number of reps
        TextView repNbField = pAct.findViewById(pRepNbId);
        repNbField.setText(String.valueOf(repNb));
        // Display the load
        TextView loadField = pAct.findViewById(pLoadId);
        loadField.setText(String.valueOf(loadKg));
        // Display the action time
        TextView timeField = pAct.findViewById(pActionTimeId);
        timeField.setText(String.valueOf(actionTime));
    }

    public void decreaseSetNb(Activity pAct) {
        currentSetNb--;
        // Display the number of sets
        TextView setNbField = pAct.findViewById(R.id.set_nb);
        setNbField.setText(String.valueOf(currentSetNb));
    }

    public void reviewModification(int pSetNb, int pRepNb, int pLoad, int pActionTime) {
        setNb = pSetNb;
        repNb = pRepNb;
        loadKg = pLoad;
        actionTime = pActionTime;
        Workout.getWorkout().modified();
    }

    public String toJSON() {
        StringBuilder json = new StringBuilder();
        String spaces = "    ";
        json.append(spaces + "{\n");
        json.append(spaces + "  \"name\": \"" + name + "\",\n");
        json.append(spaces + "  \"img\": \"" + pictureName + "\",\n");
        json.append(spaces + "  \"set_nb\":" + setNb + ",\n");
        json.append(spaces + "  \"rep_nb\":" + repNb + ",\n");
        json.append(spaces + "  \"load_kg\":" + loadKg + ",\n");
        json.append(spaces + "  \"action_sec\":" + actionTime + ",\n");
        json.append(spaces + "  \"rest_sec\":" + restTime + ",\n");
        json.append(spaces + "  \"after_sec\":" + afterTime + "\n");
        json.append(spaces + "}");
        return json.toString();
    }
}
