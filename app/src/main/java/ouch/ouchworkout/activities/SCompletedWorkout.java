package ouch.ouchworkout.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class SCompletedWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_completed_workout);
        // Display workout information
        final Workout workout = Factory.getInstance().getCurrentWorkout();
        TextView nameField = findViewById(R.id.completed_name);
        nameField.setText(workout.getName());
        workout.updateProgressBar((ProgressBar) findViewById(R.id.completed_bar));
    }
}