package ouch.ouchworkout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class ExercisePickerAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_picker);

        LinearLayout exercises = findViewById(R.id.execise_picker_list);
        final Workout workout = Workout.getWorkout();
        for (final String name : workout.getExerciseNames()) {
            final Button b = new Button(this);
            b.setText(name);
            if (workout.isWorkoutStarted()) {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Select the next exercise
                        workout.setCurrentExerciseFromName(name);
                        // Start the after countdown
                        Intent intent = new Intent(view.getContext(), AfterExerciseAct.class);
                        startActivity(intent);
                    }
                });
            } else {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Select the next exercise
                        workout.setCurrentExerciseFromName(name);
                        // Execute the first exercise
                        Intent intent = new Intent(view.getContext(), ExecutingExerciseAct.class);
                        startActivity(intent);
                    }
                });
            }
            exercises.addView(b);
        }
    }
}
