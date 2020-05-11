package ouch.ouchworkout.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class OuchWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the view and buttons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ouch_workout);
        final Activity me = this;
        // Create the factory that contains settings, list of available exercises and workouts
        Factory.initialize(this);
        final Workout workout = Factory.getInstance().getCurrentWorkout();
        if (workout != null && workout.isRunning()) {
            workout.playPause();
        }
        Button createWorkout = findViewById(R.id.create_workout);
        createWorkout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Factory.getInstance().createEmptyWorkout();
                // Display workout information
                Intent intent = new Intent(getApplicationContext(), EWorkoutExerciseSelector.class);
                startActivity(intent);
            }
        });
        Button editWorkout = findViewById(R.id.edit_workout);
        editWorkout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display workout information
                Intent intent = new Intent(view.getContext(), EExistingWorkouts.class);
                startActivity(intent);
            }
        });
        Button delWorkout = findViewById(R.id.delete_workout);
        delWorkout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display workout information
                Intent intent = new Intent(view.getContext(), EDeletingWorkouts.class);
                startActivity(intent);
            }
        });
        Button startWorkout = findViewById(R.id.start_workout);
        startWorkout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display workout information
                Intent intent = new Intent(view.getContext(), SChoosingWorkouts.class);
                startActivity(intent);
            }
        });
        Button resumeWorkout = findViewById(R.id.resume_workout);
        if (workout == null || workout.isWorkoutCompleted()) {
            resumeWorkout.setVisibility(View.INVISIBLE);
        } else {
            resumeWorkout.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    workout.resumeWorkout(me);
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
    }

    protected void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this,
                    "Write External Storage permission allows us to store workout descriptions." +
                            "Please allow this permission in App Settings.",
                    Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            Intent intent = new Intent(getApplicationContext(), SettingsEditor.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
