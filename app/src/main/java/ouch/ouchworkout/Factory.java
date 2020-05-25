package ouch.ouchworkout;


import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Factory {
    private static Factory instance = null;
    private static Settings settings = null;
    // Available exercises sorted by name
    final private static Map<String, Exercise> exercisesSName = new HashMap<>();
    // Available exercises sorted by type
    final private static Map<String, List<Exercise>> exercisesSType = new HashMap<>();
    // Available workouts sorted by type
    final private static List<Workout> workouts = new ArrayList<>();
    // Current workout (editing or running)
    private Workout currentWorkout;

    private Factory() {
    }

    public static void initialize(Activity pAct) {
        if (instance == null) {
            instance = new Factory();
            // Load exercises from RAW resources
            for (final Field f : R.raw.class.getFields()) {
                if (!f.getName().startsWith("sound_")) {
                    try {
                        InputStream jsonStream = pAct.getResources().openRawResource(f.getInt(f));
                        byte[] buffer = new byte[jsonStream.available()];
                        jsonStream.read(buffer);
                        JSONObject config = new JSONObject(new String(buffer));
                        Exercise ex = new Exercise(config);
                        if (!exercisesSType.keySet().contains(ex.getType())) {
                            exercisesSType.put(ex.getType(), new LinkedList<Exercise>());
                        }
                        exercisesSType.get(ex.getType()).add(ex);
                        exercisesSName.put(ex.getName(), ex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // Load existing JSON files: settings and workouts
            for (File f : pAct.getFilesDir().listFiles()) {
                if (f.getName().endsWith(".json")) {
                    // Read JSON files
                    if (f.getName().equals("settings.json")) {
                        // Read the settings file
                        try {
                            InputStream jsonStream = pAct.openFileInput(f.getName());
                            byte[] buffer = new byte[jsonStream.available()];
                            jsonStream.read(buffer);
                            JSONObject config = new JSONObject(new String(buffer));
                            settings = new Settings(config);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Read workout JSON files
                        try {
                            InputStream jsonStream = pAct.openFileInput(f.getName());
                            byte[] buffer = new byte[jsonStream.available()];
                            jsonStream.read(buffer);
                            JSONObject config = new JSONObject(new String(buffer));
                            workouts.add(new Workout(config));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // List existing workouts from the external directory
            try {
                File externalDir = Factory.getSettings().getExternalDirectory(pAct);
                for (File w : externalDir.listFiles()) {
                    if (w.getName().endsWith(".json")) {
                        try {
                            InputStream jsonStream = new FileInputStream(w);
                            byte[] buffer = new byte[jsonStream.available()];
                            jsonStream.read(buffer);
                            JSONObject config = new JSONObject(new String(buffer));
                            workouts.add(new Workout(config));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Load settings if it does not exist
            if (settings == null) {
                // Create the default settings file
                settings = new Settings();
                try {
                    OutputStream os = pAct.openFileOutput("settings.json", Context.MODE_PRIVATE);
                    settings.saveSettings(os);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Getters
    public static Factory getInstance() {
        return instance;
    }

    public static Settings getSettings() {
        return settings;
    }

    public List<Exercise> getExercisesFromType(String pType) {
        return exercisesSType.get(pType);
    }

    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    public Workout getCurrentWorkout() {
        return currentWorkout;
    }

    // Workout management
    public void createEmptyWorkout() {
        // Do not change the name, it allows to create EditText in EWorkoutCustomizer
        currentWorkout = new Workout("Workout Name");
    }

    public void deleteWorkout(Activity pAct, Workout pToDel) {
        workouts.remove(pToDel);
        pToDel.deleteJSON(pAct);
    }

    public void setCurrentWorkout(Workout pWo) {
        currentWorkout = pWo;
    }

    public void saveCurrentWorkout() {
        if (!workouts.contains(currentWorkout)) {
            workouts.add(currentWorkout);
        }
    }
}