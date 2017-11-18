package ouch.ouchworkout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;


public class WorkoutButton extends AppCompatButton {
    private final String workoutName;
    private final int workoutId;

    public WorkoutButton(Context context, final String pText, final int pJSONFileId) {
        super(context);
        setText(pText);
        workoutName = pText;
        workoutId = pJSONFileId;
    }

    public String getWorkoutName() {
        return workoutName;
    }
    public int getWorkoutId() {
        return workoutId;
    }
}
