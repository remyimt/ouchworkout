package ouch.ouchworkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class WorkoutAct extends AppCompatActivity {
    private Map<String, JSONArray> name2Description = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_workout);

        // Load the settings from 'settings.json'
        String[] myfiles = getFilesDir().list();
        boolean existSettings = false;
        for (String s : myfiles) {
            if (s.equals(SettingsAct.SETTINGS_FILE)) {
                existSettings = true;
            }
        }
        if (existSettings) {
            // Load the settings properties
            try {
                Settings.loadSettings(openFileInput(SettingsAct.SETTINGS_FILE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Create the settings file
            try {
                OutputStream os = openFileOutput(SettingsAct.SETTINGS_FILE, MODE_PRIVATE);
                Settings.getSettings().saveSettings(os);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Pause the current workout
        if (Workout.hasWorkout()) {
            Workout w = Workout.getWorkout();
            if (w.isRunning()) {
                w.playPause();
            }
        }
        // List of existing workouts
        Field[] fields = R.raw.class.getFields();
        for (Field f : fields) {
            try {
                if (f.getName().endsWith("_wo")) {
                    InputStream is = getResources().openRawResource(f.getInt(f));
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    is.close();
                    JSONObject myWorkout = new JSONObject(new String(buffer));
                    name2Description.put(myWorkout.getString("name"),
                            myWorkout.getJSONArray("workout"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        Workout workout = Workout.getWorkout();
        String workoutName = "";
        if (Workout.hasWorkout()) {
            workoutName = workout.getName();
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.workout_list);
        layout.removeAllViews();
        for (final String s : name2Description.keySet()) {
            final Button b = new Button(this);
            if (s.equals(workoutName) && workout.isInProgress() && workout.isWorkoutStarted()) {
                // This workout is already loaded
                b.setText("** " + s + " **");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Display the workout
                        Intent intent = new Intent(view.getContext(), ExecutingNextExerciseAct.class);
                        startActivity(intent);
                    }
                });
            } else {
                b.setText(s);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            // Load the workout
                            Workout.createWorkout(s, name2Description.get(s));
                            // Display the exercise selection
                            Intent intent = new Intent(view.getContext(), ExerciseSelectionAct.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            b.setText("FAILED!");
                            e.printStackTrace();
                        }
                    }
                });
            }
            layout.addView(b);
        }
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
            Intent intent = new Intent(getApplicationContext(), SettingsAct.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
