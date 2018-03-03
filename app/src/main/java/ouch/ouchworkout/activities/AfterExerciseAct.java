package ouch.ouchworkout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;
import ouch.ouchworkout.countdown.AfterCountdown;

public class AfterExerciseAct extends AppCompatActivity {
    private AfterCountdown afterCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_exercise);
        Workout workout = Workout.getWorkout();
        // Display exercise information
        Exercise exe = workout.getCurrentExercise();
        exe.display(this, R.id.after_name, R.id.after_set_nb, R.id.after_rep_nb,
                R.id.after_img, R.id.after_load_container);
        workout.updateProgressBar((ProgressBar) findViewById(R.id.after_workout_bar));
        // Configure the review button
        Button reviewButton = (Button) findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterCd.cancel();
                Intent intent = new Intent(view.getContext(), ReviewerAct.class);
                startActivity(intent);
            }
        });
        // Start the after countdown
        afterCd = new AfterCountdown(this, workout.getLastCompletedExercise().getAfterTime());
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
            Workout.getWorkout().resumeWorkout(this);
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
