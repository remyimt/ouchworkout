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

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class OuchWorkout extends AppCompatActivity {
    private Map<String, JSONArray> name2Description = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_workout);

        // Pause the current workout
        if(Workout.hasWorkout()){
            Workout w = Workout.getWorkout();
            if(w.isRunning()){
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
        String workoutName = "";
        if (Workout.hasWorkout()) {
            workoutName = Workout.getWorkout().getName();
        }
        for (final String s : name2Description.keySet()) {
            final Button b = new Button(this);
            if (s.equals(workoutName) &&
                    Workout.getWorkout().getCurrentExercise().getExerciseNb() > 0) {
                // This workout is already loaded
                b.setText("** " + s + " **");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Display the workout
                        Intent intent = new Intent(view.getContext(), ExecutingWorkout.class);
                        startActivity(intent);
                    }
                });
            } else {
                b.setText(s);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Load the workout
                        try {
                            Workout.createWorkout(s, name2Description.get(s));
                            // Display the workout
                            Intent intent = new Intent(view.getContext(), ExecutingWorkout.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            b.setText("FAILED!");
                            e.printStackTrace();
                        }
                    }
                });
            }
            LinearLayout layout = (LinearLayout) findViewById(R.id.workout_list);
            layout.addView(b);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button) {
            Intent intent = new Intent(getApplicationContext(), OuchSettings.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
