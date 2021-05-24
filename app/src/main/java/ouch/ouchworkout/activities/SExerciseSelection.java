package ouch.ouchworkout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class SExerciseSelection extends AppCompatActivity {
    private List<Integer> removeExerciseIdx = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_exercise_selection);
        // Display the list of exercises
        Workout w = Factory.getInstance().getCurrentWorkout();
        LinearLayout exerciseList = findViewById(R.id.exercise_list);
        exerciseList.removeAllViews();
        for (Exercise ex : w.getExercises()) {
            CheckBox box = new CheckBox(this);
            box.setText(ex.getName());
            box.setChecked(true);
            exerciseList.addView(box);
        }
        // Configure the button
        Button doneButton = findViewById(R.id.select_exercise_button);
        doneButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Remove unchecked exercises
                Workout w = Factory.getInstance().getCurrentWorkout();
                LinearLayout exerciseList = findViewById(R.id.exercise_list);
                int count;
                if (removeExerciseIdx.isEmpty()) {
                    count = exerciseList.getChildCount();
                } else {
                    // Do not get the last stretch_child (TextView with removed exercises)
                    count = exerciseList.getChildCount() - 1;
                }
                for (int i = 0; i < count; i++) {
                    CheckBox c = (CheckBox) exerciseList.getChildAt(i);
                    if (!c.isChecked()) {
                        // List of integers (indexes) in descending order
                        removeExerciseIdx.add(0, i);
                    }
                }
                w.setRunningExercises(removeExerciseIdx);
                if (w.getRunningExercises().isEmpty()) {
                    // Back to the home page
                    Intent intent = new Intent(v.getContext(), OuchWorkout.class);
                    startActivity(intent);
                } else {
                    Factory.getInstance().getCurrentWorkout().initialize();
                    // Display workout information
                    Intent intent = new Intent(v.getContext(), SWorkoutInformation.class);
                    startActivity(intent);
                }
            }
        });
    }
}
