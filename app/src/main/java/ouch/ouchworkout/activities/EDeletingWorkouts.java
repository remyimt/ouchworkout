package ouch.ouchworkout.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class EDeletingWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity currentActivity = this;
        setContentView(R.layout.existing_workouts);
        LinearLayout layout = findViewById(R.id.existing_wo);
        layout.removeAllViews();
        for (final Workout wo : Factory.getInstance().getWorkouts()) {
            Button b = new Button(getApplicationContext());
            b.setText(wo.getName());
            b.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Factory.getInstance().deleteWorkout(currentActivity, wo);
                    // Display workout information
                    Intent intent = new Intent(getApplicationContext(), EDeletingWorkouts.class);
                    startActivity(intent);
                }
            });
            layout.addView(b);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}