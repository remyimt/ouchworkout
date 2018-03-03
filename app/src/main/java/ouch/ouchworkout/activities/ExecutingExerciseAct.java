package ouch.ouchworkout.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;
import ouch.ouchworkout.countdown.AbstractCountdown;

public class ExecutingExerciseAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executing_exercise);
        Workout workout = Workout.getWorkout();
        Exercise exe = workout.getCurrentExercise();
        exe.display(this, R.id.exercise_name, R.id.set_nb, R.id.rep_nb,
                R.id.exercise_img, R.id.load_container);
        workout.updateProgressBar((ProgressBar) findViewById(R.id.workout_bar));
        workout.loadExercise(this);
        TextView countdownField = (TextView) findViewById(R.id.countdown);
        if (workout.isRunning()) {
            countdownField.setText(String.valueOf(exe.getActionTime()));
            workout.startExercise();
        } else {
            countdownField.setText(AbstractCountdown.formatCountdown(workout.getResumeTime()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Workout workout = Workout.getWorkout();
        getMenuInflater().inflate(R.menu.action_menu, menu);
        // Set the menu item icons
        if (!workout.isRunning()) {
            menu.getItem(0).setIcon(android.R.drawable.ic_media_play);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Workout w = Workout.getWorkout();
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
        Workout w = Workout.getWorkout();
        if (w.isRunning()) {
            w.playPause();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Workout w = Workout.getWorkout();
        if (w.isRunning()) {
            w.playPause();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        Workout w = Workout.getWorkout();
        if (w.isRunning()) {
            w.playPause();
        }
        super.onPause();
    }
}
