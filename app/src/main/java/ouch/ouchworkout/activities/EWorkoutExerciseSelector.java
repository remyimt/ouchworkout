package ouch.ouchworkout.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ouch.ouchworkout.Exercise;
import ouch.ouchworkout.Factory;
import ouch.ouchworkout.R;

public class EWorkoutExerciseSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_workout_exercise_selector);
        // Get the exercise list container
        final LinearLayout exercise_list = findViewById(R.id.all_exercises);
        exercise_list.removeAllViews();
        // Configure the done button
        Button doneButton = findViewById(R.id.n_w_p_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EWorkoutOrganizer.class);
                startActivity(intent);
            }
        });
        // Configure the filter buttons
        Button strength = findViewById(R.id.strength_filter);
        strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayExercises(((Button) view).getText().toString(), exercise_list);
            }
        });
        Button stretch = findViewById(R.id.stretch_filter);
        stretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayExercises(((Button) view).getText().toString(), exercise_list);
            }
        });
        Button machine = findViewById(R.id.machine_filter);
        machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayExercises(((Button) view).getText().toString(), exercise_list);
            }
        });
        Button dumbbell = findViewById(R.id.dumbbell_filter);
        dumbbell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayExercises(((Button) view).getText().toString(), exercise_list);
            }
        });
        displayExercises("strength", exercise_list);
    }

    private void displayExercises(String pType, LinearLayout pExerciseList) {
        pExerciseList.removeAllViews();
        for (final Exercise ex : Factory.getInstance().getExercisesFromType(pType)) {
            LinearLayout container = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 10);
            container.setLayoutParams(lp);
            container.setOrientation(LinearLayout.VERTICAL);
            // Add the name and the exercise counter
            LinearLayout nameCounter = new LinearLayout(getApplicationContext());
            TextView exName = new TextView(getApplicationContext());
            exName.setText(ex.getName());
            exName.setTextSize(18);
            LinearLayout.LayoutParams nameParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 2);
            exName.setLayoutParams(nameParams);
            final TextView exCounter = new TextView(getApplicationContext());
            exCounter.setText("#0");
            exCounter.setTextSize(18);
            exCounter.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams counterParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            exCounter.setLayoutParams(counterParams);
            nameCounter.addView(exName);
            nameCounter.addView(exCounter);
            container.addView(nameCounter);
            // Add the image
            ImageView img = new ImageView(getApplicationContext());
            img.setImageResource(getResources().getIdentifier(
                    ex.getPictureName(), "drawable", getPackageName()));
            LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    200);
            imgLp.gravity = Gravity.CENTER;
            img.setLayoutParams(imgLp);
            container.addView(img);
            // Add button
            Button add = new Button(getApplicationContext());
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button me = (Button) view;
                    if (me.getText().equals("add")) {
                        ((LinearLayout) view.getParent()).setBackgroundColor(Color.parseColor("#cfecfc"));
                        ((Button) view).setText("remove");
                        Factory.getInstance().getCurrentWorkout().addExercise(ex);
                        int nbEx = Integer.parseInt(
                                exCounter.getText().subSequence(1, exCounter.getText().length()).toString());
                        exCounter.setText("#" + (nbEx + 1));
                    } else {
                        ((LinearLayout) view.getParent()).setBackgroundResource(android.R.color.background_light);
                        ((Button) view).setText("add");
                        Factory.getInstance().getCurrentWorkout().removeExercise(ex);
                        int nbEx = Integer.parseInt(
                                exCounter.getText().subSequence(1, exCounter.getText().length()).toString());
                        exCounter.setText("#" + (nbEx - 1));
                    }
                }
            });
            add.setText("add");
            container.addView(add);
            pExerciseList.addView(container);
            // Check if the exercise is already in the workout
            if (Factory.getInstance().getCurrentWorkout().containsExercise(ex)) {
                ((LinearLayout) add.getParent()).setBackgroundColor(Color.parseColor("#cfecfc"));
                add.setText("remove");
                int nbEx = Integer.parseInt(
                        exCounter.getText().subSequence(1, exCounter.getText().length()).toString());
                exCounter.setText("#" + (nbEx + 1));
            }
        }
    }
}