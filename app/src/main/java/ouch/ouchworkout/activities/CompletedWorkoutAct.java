package ouch.ouchworkout.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Settings;
import ouch.ouchworkout.Workout;

public class CompletedWorkoutAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_workout);
        // Display workout information
        final Workout workout = Workout.getWorkout();
        TextView nameField = findViewById(R.id.completed_name);
        nameField.setText(workout.getName());
        workout.updateProgressBar((ProgressBar) findViewById(R.id.completed_bar));
        // Configure the review button
        Button review = findViewById(R.id.completed_review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReviewerAct.class);
                startActivity(intent);
            }
        });
        // Configure the save workout button
        Button save = findViewById(R.id.save_workout);
        TextView incomplete = findViewById(R.id.incomplete_text);
        if (workout.isModified()) {
            if (!workout.isIncomplete()) {
                incomplete.setVisibility(View.INVISIBLE);
            }
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        File external = new File(Settings.getSettings().getExternalDirectory(),
                                workout.getFilename() + ".json");
                        FileOutputStream output = new FileOutputStream(external);
                        PrintWriter writer = new PrintWriter(output);
                        writer.print(workout.toJSON());
                        writer.flush();
                        writer.close();
                        output.close();
                        // Try to add the file to the media scanner
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(external));
                        sendBroadcast(intent);
                        Intent intent2 = new Intent(view.getContext(), WorkoutAct.class);
                        startActivity(intent2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            incomplete.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
        }
    }
}
