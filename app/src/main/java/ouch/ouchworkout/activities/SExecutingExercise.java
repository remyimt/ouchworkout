package ouch.ouchworkout.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;
import ouch.ouchworkout.countdown.AbstractCountdown;

public class SExecutingExercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_executing_exercise);
        Workout workout = Factory.getInstance().getCurrentWorkout();
        Exercise exe = workout.getCurrentRunningExercise();
        exe.display(this, R.id.exercise_name, R.id.set_nb, R.id.rep_nb,
                R.id.exercise_img, R.id.load_container);
        workout.updateProgressBar((ProgressBar) findViewById(R.id.workout_bar));
        workout.loadExercise(this);
        TextView countdownField = findViewById(R.id.countdown);
        if (workout.isRunning()) {
            countdownField.setText(String.valueOf(exe.getActionTime()));
            workout.startExercise(this);
        } else {
            countdownField.setText(AbstractCountdown.formatCountdown(workout.getResumeTime()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Workout workout = Factory.getInstance().getCurrentWorkout();
        getMenuInflater().inflate(R.menu.action_menu, menu);
        // Set the menu item icons
        if (!workout.isRunning()) {
            menu.getItem(0).setIcon(android.R.drawable.ic_media_play);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Workout w = Factory.getInstance().getCurrentWorkout();
        if (item.getItemId() == R.id.play_pause) {
            if (w.isActionPhase() && w.getCurrentExercise().isDoneButtonRequired()) {
                // Nothing to do, no countdown
            } else {
                if (w.isRunning()) {
                    item.setIcon(android.R.drawable.ic_media_play);
                } else {
                    item.setIcon(android.R.drawable.ic_media_pause);
                }
                w.playPause();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Workout w = Factory.getInstance().getCurrentWorkout();
        if (w.isRunning()) {
            w.playPause();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Workout w = Factory.getInstance().getCurrentWorkout();
        if (w.isRunning()) {
            w.playPause();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        Workout w = Factory.getInstance().getCurrentWorkout();
        if (w.isRunning()) {
            w.playPause();
        }
        super.onPause();
    }
}
