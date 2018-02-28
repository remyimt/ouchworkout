package ouch.ouchworkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class ExerciseSelectionAct extends AppCompatActivity {
    private List<String> removeExercises = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection);
        // Configure the button
        Button doneButton = (Button) findViewById(R.id.select_exercise_button);
        doneButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // Remove unchecked exercises
                Workout w = Workout.getWorkout();
                LinearLayout exerciseList = (LinearLayout) findViewById(R.id.exercise_list);
                int count;
                if (removeExercises.isEmpty()) {
                    count = exerciseList.getChildCount();
                } else {
                    // Do not get the last child (TextView with removed exercises)
                    count = exerciseList.getChildCount() - 1;
                }
                for (int i = 0; i < count; i++) {
                    CheckBox c = (CheckBox) exerciseList.getChildAt(i);
                    if (!c.isChecked()) {
                        removeExercises.add(c.getText().toString());
                    }
                }
                w.removeExerciseFromNames(removeExercises);
                if (w.getExerciseNames().isEmpty()) {
                    // Back to the workout selection
                    Intent intent = new Intent(v.getContext(), WorkoutAct.class);
                    startActivity(intent);
                } else {
                    // Display the workout
                    Intent intent = new Intent(v.getContext(), ExecutingNextExerciseAct.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        // Display the list of exercises
        Workout w = Workout.getWorkout();
        LinearLayout exerciseList = (LinearLayout) findViewById(R.id.exercise_list);
        exerciseList.removeAllViews();
        for (String ex : w.getExerciseNames()) {
            CheckBox box = new CheckBox(this);
            box.setText(ex);
            box.setChecked(true);
            exerciseList.addView(box);
        }
        if (!removeExercises.isEmpty()) {
            TextView notAvailable = new TextView(this);
            StringBuilder toDisplay = new StringBuilder();
            for (String s : removeExercises) {
                toDisplay.append(s + ", ");
            }
            toDisplay.setLength(toDisplay.length() - 2);
            notAvailable.setText("Previously removed:\n" + toDisplay);
            exerciseList.addView(notAvailable);
        }
        super.onResume();
    }
}
