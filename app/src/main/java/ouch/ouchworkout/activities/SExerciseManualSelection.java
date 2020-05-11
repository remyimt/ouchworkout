package ouch.ouchworkout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class SExerciseManualSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_manual_selection);

        LinearLayout exercises = findViewById(R.id.execise_picker_list);
        final Workout workout = Factory.getInstance().getCurrentWorkout();
        for (final Exercise ex : workout.getRunningExercises()) {
            if (!ex.isCompleted()) {
                final Button b = new Button(this);
                b.setText(ex.getName());
                if (workout.isWorkoutStarted()) {
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Select the next exercise
                            workout.setRunningExercise(ex);
                            // Start the after countdown
                            Intent intent = new Intent(view.getContext(), SAfterExercise.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Select the next exercise
                            workout.setRunningExercise(ex);
                            // Execute the first exercise
                            Intent intent = new Intent(view.getContext(), SExecutingExercise.class);
                            startActivity(intent);
                        }
                    });
                }
                exercises.addView(b);
            }
        }
    }
}
