package ouch.ouchworkout.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Settings;
import ouch.ouchworkout.Workout;

public class WorkoutAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_workout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        // Load the settings from 'settings.json'
        boolean existSettings = false;
        for (String s : getFilesDir().list()) {
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
        createCategoryButtons();
    }

    private void createCategoryButtons() {
        LinearLayout layout = findViewById(R.id.workout_list);
        layout.removeAllViews();
        // Add the legs button
        Button legs = new Button(this);
        legs.setText("LEGS");
        legs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWorkouts("legs");
            }
        });
        layout.addView(legs);
        // Add the upper body button
        Button upper = new Button(this);
        upper.setText("UPPER BODY");
        upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWorkouts("upper");
            }
        });
        layout.addView(upper);
        // Add the mixed button
        Button mixed = new Button(this);
        mixed.setText("MIXED");
        mixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWorkouts("mixed");
            }
        });
        layout.addView(mixed);
        // Add the with machines button
        Button wmachines = new Button(this);
        wmachines.setText("WITH MACHINES");
        wmachines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWorkouts("wmachines");
            }
        });
        layout.addView(wmachines);
        // Add the stretching button
        Button stretching = new Button(this);
        stretching.setText("STRETCHING");
        stretching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWorkouts("stretching");
            }
        });
        layout.addView(stretching);
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

    private Button createWorkoutButton(final String pFilename, InputStream pInput,
                                       Map<Integer, List<Button>> pExistingButtons)
            throws IOException, JSONException {
        // Get the name of the existing workout
        Workout workout = Workout.getWorkout();
        String workoutName = "";
        if (Workout.hasWorkout()) {
            workoutName = workout.getName();
        }
        // Read the JSON file
        byte[] buffer = new byte[pInput.available()];
        pInput.read(buffer);
        pInput.close();
        final JSONObject myWorkout = new JSONObject(new String(buffer));
        final Button b = new Button(this);
        int difficulty = myWorkout.getInt("difficulty");
        if (!pExistingButtons.containsKey(difficulty)) {
            pExistingButtons.put(difficulty, new LinkedList<Button>());
        }
        pExistingButtons.get(difficulty).add(b);
        if (myWorkout.getString("name").equals(workoutName) && workout.isInProgress() &&
                workout.isWorkoutStarted()) {
            b.setText("** " + workoutName + " **");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Display the workout
                    Intent intent = new Intent(view.getContext(), ExecutingExerciseAct.class);
                    startActivity(intent);
                }
            });
        } else {
            b.setText(myWorkout.getString("name"));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Load the workout
                        Workout.createWorkout(pFilename,
                                myWorkout.getString("name"),
                                myWorkout.getInt("difficulty"),
                                myWorkout.getJSONArray("workout"));
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
        return b;
    }

    private void listWorkouts(String pFilter) {
        List<String> existingButtonNames = new LinkedList<>();
        // Button by levels of difficulty
        Map<Integer, List<Button>> existingButtons = new LinkedHashMap<>();
        existingButtons.put(1, new LinkedList<Button>());
        existingButtons.put(2, new LinkedList<Button>());
        existingButtons.put(3, new LinkedList<Button>());
        // Remove existing buttons
        LinearLayout layout = findViewById(R.id.workout_list);
        layout.removeAllViews();
        // List existing workouts from the external directory
        try {
            File externalDir = Settings.getSettings().getExternalDirectory(this);
            for (File w : externalDir.listFiles()) {
                if (w.getName().startsWith("wo_" + pFilter)) {
                    String filename = w.getName().substring(0, w.getName().indexOf('.'));
                    try {
                        createWorkoutButton(filename, new FileInputStream(w), existingButtons);
                        existingButtonNames.add(filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // List existing workouts from the internal directory
        for (final Field f : R.raw.class.getFields()) {
            try {
                if (f.getName().startsWith("wo_" + pFilter) &&
                        !existingButtonNames.contains(f.getName())) {
                    createWorkoutButton(f.getName(), getResources().openRawResource(f.getInt(f)),
                            existingButtons);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Map.Entry<Integer, List<Button>> buttons : existingButtons.entrySet()) {
            if (!buttons.getValue().isEmpty()) {
                TextView difficulty = new TextView(this);
                difficulty.setText("Difficulty - " + buttons.getKey());
                difficulty.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                difficulty.setTextColor(Color.BLACK);
                layout.addView(difficulty);
                for (Button b : buttons.getValue()) {
                    layout.addView(b);
                }
            }
        }
        Button mainMenu = new Button(this);
        mainMenu.setText("MAIN MENU");
        mainMenu.setTextColor(Color.BLUE);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCategoryButtons();
            }
        });
        layout.addView(mainMenu);
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
            Intent intent = new Intent(getApplicationContext(), SettingsAct.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
