package ouch.ouchworkout.activities;

import android.content.Intent;
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

import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class CompletedWorkoutAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_workout);
        // Display workout information
        final Workout workout = Workout.getWorkout();
        TextView nameField = (TextView) findViewById(R.id.completed_name);
        nameField.setText(workout.getName());
        workout.updateProgressBar((ProgressBar) findViewById(R.id.completed_bar));
        // Configure the review button
        Button review = (Button) findViewById(R.id.completed_review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReviewerAct.class);
                startActivity(intent);
            }
        });
        // Configure the save workout button
        Button save = (Button) findViewById(R.id.save_workout);
        TextView incomplete = (TextView) findViewById(R.id.incomplete_text);
        if (workout.isModified()) {
            if (!workout.isIncomplete()) {
                incomplete.setVisibility(View.INVISIBLE);
            }
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        File external = new File(getExternalFilesDir(null),
                                workout.getFilename() + ".json");
                        FileOutputStream output = new FileOutputStream(external);
                        output.write(workout.toJSON().getBytes());
                        output.flush();
                        output.close();
                        Intent intent = new Intent(view.getContext(), WorkoutAct.class);
                        startActivity(intent);
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
