package ouch.ouchworkout.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class ReviewerAct extends AppCompatActivity {
    private Activity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        setContentView(R.layout.activity_reviewer);
        // Display exercise information
        final Workout workout = Workout.getWorkout();
        final Exercise exe = workout.getLastCompletedExercise();
        exe.shortDisplay(this, R.id.review_name, R.id.review_set, R.id.review_rep,
                R.id.review_load, R.id.review_time);
        // Configure buttons to modify the workout
        setButton(R.id.dec_setnb, R.id.review_set, -1);
        setButton(R.id.inc_setnb, R.id.review_set, 1);
        setButton(R.id.dec_repnb, R.id.review_rep, -2);
        setButton(R.id.inc_repnb, R.id.review_rep, 2);
        setButton(R.id.dec_load, R.id.review_load, -5);
        setButton(R.id.inc_load, R.id.review_load, 5);
        setButton(R.id.dec_time, R.id.review_time, -2);
        setButton(R.id.inc_time, R.id.review_time, 2);
        // Configure the done button
        Button doneButton = (Button) findViewById(R.id.save_review);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check modifications
                int setNb = Integer.parseInt(((TextView) findViewById(R.id.review_set))
                        .getText().toString());
                int repNb = Integer.parseInt(((TextView) findViewById(R.id.review_rep))
                        .getText().toString());
                int load = Integer.parseInt(((TextView) findViewById(R.id.review_load))
                        .getText().toString());
                int time = Integer.parseInt(((TextView) findViewById(R.id.review_time))
                        .getText().toString());
                if (setNb != exe.getSetNb() || repNb != exe.getRepNb() || load != exe.getLoadKg()
                        || time != exe.getActionTime()) {
                    // Modify the exercise
                    exe.reviewModification(setNb, repNb, load, time);
                }
                // Continue the workout
                if (workout.isInProgress()) {
                    Workout.getWorkout().resumeWorkout(me);
                } else {
                    Intent intent = new Intent(view.getContext(), CompletedWorkoutAct.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setButton(int buttonId, int valueId, final int modifier) {
        final Button b = (Button) findViewById(buttonId);
        final TextView v = (TextView) findViewById(valueId);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(v.getText().toString());
                v.setText(String.valueOf(value + modifier));
            }
        });
    }
}
