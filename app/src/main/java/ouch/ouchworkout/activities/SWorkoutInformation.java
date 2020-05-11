package ouch.ouchworkout.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Settings;
import ouch.ouchworkout.Workout;

public class SWorkoutInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_workout_information);
        final Workout workout = Factory.getInstance().getCurrentWorkout();
        final Activity me = this;
        final Settings settings = Factory.getSettings();
        // Name of the workout
        TextView nameField = findViewById(R.id.workout_name);
        nameField.setText(workout.getName());
        // Number of exercises
        TextView exerciseNb = findViewById(R.id.exercise_nb);
        exerciseNb.setText(workout.getRunningExercises().size() + " exercises");
        // Workout duration
        TextView time = findViewById(R.id.workout_time);
        time.setText(workout.getLengthMinutes() + " min " + workout.getLengthSeconds() + "s");
        // Settings
        if (settings.isWithSound()) {
            ImageView sound = findViewById(R.id.sound_settings);
            sound.setImageResource(R.drawable.with_sound);
        }
        if (settings.isManualSelection()) {
            ImageView selection = findViewById(R.id.selection_settings);
            selection.setImageResource(R.drawable.manual_selection);
        }
        // Configure the start button
        Button start = findViewById(R.id.workout_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the workout
                if (Factory.getSettings().isManualSelection()) {
                    // Manually select the first exercise of the workout
                    Intent intent = new Intent(getApplicationContext(), SExerciseManualSelection.class);
                    startActivity(intent);
                } else {
                    // Display the first exercise
                    workout.resumeWorkout(me);
                }
            }
        });
    }
}
