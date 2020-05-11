package ouch.ouchworkout.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;
import ouch.ouchworkout.countdown.AfterCountdown;

public class SAfterExercise extends AppCompatActivity {
    private AfterCountdown afterCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_after_exercise);
        Workout workout = Factory.getInstance().getCurrentWorkout();
        // Display exercise information
        Exercise exe = workout.getCurrentRunningExercise();
        exe.display(this, R.id.after_name, R.id.after_set_nb, R.id.after_rep_nb,
                R.id.after_img, R.id.after_load_container);
        workout.updateProgressBar((ProgressBar) findViewById(R.id.after_workout_bar));
        // Start the after countdown
        afterCd = new AfterCountdown(this, workout.getPreviousRunningExercise().getRecoverTime());
        afterCd.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.play_pause) {
            afterCd.cancel();
            Factory.getInstance().getCurrentWorkout().resumeWorkout(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        afterCd.cancel();
        super.onPause();
    }
}
