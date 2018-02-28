package ouch.ouchworkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ExecutingNextExerciseAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executing_next_exercise);
        Workout workout = Workout.getWorkout();
        if (workout.isInProgress()) {
            // Resume an existing workout
            workout.startCurrentExercise(this, false);
        } else if (workout.selectNextExercise()) {
            if (workout.isFirstExercise()) {
                workout.startCurrentExercise(this, false);
            } else {
                workout.startCurrentExercise(this, true);
            }
        } else {
            workout.complete(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        if (Workout.getWorkout().isRunning()) {
            menu.getItem(0).setIcon(android.R.drawable.ic_media_pause);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Workout w = Workout.getWorkout();
        if (item.getItemId() == R.id.play_pause) {
            if (w.isActionPhase() && w.isDoneButtonRequired()) {
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
}
