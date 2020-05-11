package ouch.ouchworkout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;
import ouch.ouchworkout.Workout;

public class EWorkoutCustomizer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_workout_customizer);
        final AppCompatActivity currentActivity = this;
        final Workout workout = Factory.getInstance().getCurrentWorkout();
        final Exercise exercise = workout.getCurrentExercise();
        // Retrieve all input fields
        final TextView woName = findViewById(R.id.custom_wo_name);
        woName.setText(workout.getName());
        //woName.setText(workout.getName());
        final TextView exName = findViewById(R.id.custom_name);
        final TextView setNb = findViewById(R.id.custom_set_nb);
        final TextView repNb = findViewById(R.id.custom_rep_nb);
        final TextView loadKg = findViewById(R.id.custom_load_kg);
        final TextView actionSec = findViewById(R.id.custom_action_sec);
        final TextView restSec = findViewById(R.id.custom_rest_sec);
        final TextView recoverSec = findViewById(R.id.custom_recover_sec);
        // ShowImage button
        final LinearLayout container = findViewById(R.id.custom_container);
        final Button showImage = findViewById(R.id.custom_image);
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button me = (Button) view;
                if (me.getText().equals("show image")) {
                    me.setText("remove image");
                    ImageView img = new ImageView(getApplicationContext());
                    img.setImageResource(getResources().getIdentifier(
                            workout.getCurrentExercise().getPictureName(), "drawable", getPackageName()));
                    container.addView(img, 2);
                } else {
                    me.setText("show image");
                    container.removeViewAt(2);
                }
            }
        });
        // Configure the next button
        Button next = findViewById(R.id.custom_next);
        final EditText woNameEdit = new EditText(getApplicationContext());
        if (workout.isLastExercise()) {
            next.setText("save workout");
            if (woName.getText().equals("Workout Name")) {
                // Replace TextView by EditText to set the workout name
                woNameEdit.setText(woName.getText());
                container.removeViewAt(0);
                container.addView(woNameEdit, 0);
            }
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button me = (Button) view;
                // Check there is no empty fields
                if (setNb.getText().length() == 0) {
                    setNb.requestFocus();
                    return;
                }
                if (repNb.getText().length() == 0) {
                    repNb.requestFocus();
                    return;
                }
                if (loadKg.getText().length() == 0) {
                    loadKg.requestFocus();
                    return;
                }
                if (actionSec.getText().length() == 0) {
                    actionSec.requestFocus();
                    return;
                }
                if (restSec.getText().length() == 0) {
                    restSec.requestFocus();
                    return;
                }
                if (recoverSec.getText().length() == 0) {
                    recoverSec.requestFocus();
                    return;
                }
                Exercise ex = workout.getCurrentExercise();
                ex.setSetNb(Integer.parseInt(setNb.getText().toString()));
                ex.setRepNb(Integer.parseInt(repNb.getText().toString()));
                ex.setLoadKg(Integer.parseInt(loadKg.getText().toString()));
                ex.setActionTime(Integer.parseInt(actionSec.getText().toString()));
                ex.setRestTime(Integer.parseInt(restSec.getText().toString()));
                ex.setRecoverTime(Integer.parseInt(recoverSec.getText().toString()));
                // Configure button actions
                if (me.getText().equals("next exercise")) {
                    workout.getNextExercise();
                    Intent intent = new Intent(view.getContext(), EWorkoutCustomizer.class);
                    startActivity(intent);
                } else {
                    if (woNameEdit.getParent() != null) {
                        // Set the workout name
                        if (woNameEdit.getText().length() == 0 ||
                                woNameEdit.getText().toString().equals("Workout Name")) {
                            woNameEdit.requestFocus();
                            return;
                        } else {
                            workout.setName(woNameEdit.getText().toString());
                        }
                    }
                    workout.saveJSON(currentActivity);
                    Intent intent = new Intent(view.getContext(), OuchWorkout.class);
                    startActivity(intent);
                }
            }
        });
        // Load the current exercise
        exName.setText(exercise.getName());
        setNb.setText(String.valueOf(exercise.getSetNb()));
        repNb.setText(String.valueOf(exercise.getRepNb()));
        loadKg.setText(String.valueOf(exercise.getLoadKg()));
        actionSec.setText(String.valueOf(exercise.getActionTime()));
        restSec.setText(String.valueOf(exercise.getRestTime()));
        recoverSec.setText(String.valueOf(exercise.getRecoverTime()));
        // Set the focus to the first input field
        setNb.requestFocus();
    }
}