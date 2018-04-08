package ouch.ouchworkout.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Settings;
import ouch.ouchworkout.Workout;

public class WorkoutStartupAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_startup);
        final Workout workout = Workout.getWorkout();
        final Activity me = this;
        final Settings settings = Settings.getSettings();
        // Name of the workout
        TextView nameField = (TextView) findViewById(R.id.workout_name);
        nameField.setText(workout.getName());
        // Number of exercises
        TextView exerciseNb = (TextView) findViewById(R.id.exercise_nb);
        exerciseNb.setText(workout.getExerciseNames().size() + " exercises");
        // Workout duration
        TextView time = (TextView) findViewById(R.id.workout_time);
        time.setText(workout.getLengthMinutes() + " min " + workout.getLengthSeconds() + "s");
        // Settings
        if (settings.isWithSound()) {
            ImageView sound = (ImageView) findViewById(R.id.sound_settings);
            sound.setImageResource(R.drawable.with_sound);
        }
        if (settings.isManualSelection()) {
            ImageView selection = (ImageView) findViewById(R.id.selection_settings);
            selection.setImageResource(R.drawable.manual_selection);
        }
        // Configure the start button
        Button start = (Button) findViewById(R.id.workout_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Copy the workout JSON file to the external directory
                File external = new File(Settings.getSettings().getExternalDirectory(),
                        workout.getFilename() + ".json");
                try {
                    if (!external.exists()) {
                        FileOutputStream output = new FileOutputStream(external);
                        PrintWriter writer = new PrintWriter(output);
                        writer.print(workout.toJSON());
                        writer.flush();
                        writer.close();
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Try to add the file to the media scanner
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(external));
                sendBroadcast(intent);
                // Start the workout
                workout.selectNextExercise(me);
            }
        });
    }
}
