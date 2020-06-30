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
import ouch.ouchworkout.Workout;

public class EWorkoutOrganizer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_workout_organizer);
        final Workout current = Factory.getInstance().getCurrentWorkout();
        // Configure the done button
        Button done = findViewById(R.id.w_o_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.getFirstExercise();
                Intent intent = new Intent(getApplicationContext(), EWorkoutCustomizer.class);
                startActivity(intent);
            }
        });
        // Get the exercise list container
        final LinearLayout exercise_list = findViewById(R.id.exercise_desc);
        exercise_list.removeAllViews();
        for (Exercise ex : current.getExercises()) {
            appendExercise(current, ex, exercise_list);
        }
    }

    private void appendExercise(final Workout workout, final Exercise pEx, LinearLayout pExerciseDesc) {
        // Exercise element container
        final LinearLayout container = new LinearLayout(getApplicationContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 10);
        container.setLayoutParams(lp);
        container.setBackgroundColor(Color.parseColor("#cfecfc"));
        // Name of the exercise
        TextView exName = new TextView(getApplicationContext());
        exName.setText(pEx.getName());
        exName.setTextSize(22);
        exName.setGravity(Gravity.CENTER);
        exName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // Up button
        Button up = new Button(getApplicationContext());
        up.setText("up");
        up.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout main = (LinearLayout) container.getParent();
                int index = main.indexOfChild(container);
                if (index > 0) {
                    if (workout.moveUpExercise(index)) {
                        main.removeView(container);
                        main.addView(container, index - 1);
                    }
                }
            }
        });
        // Show image button
        Button showImage = new Button(getApplicationContext());
        showImage.setText("show image");
        showImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button me = (Button) view;
                if (me.getText() == "show image") {
                    me.setText("remove image");
                    ImageView img = new ImageView(getApplicationContext());
                    img.setImageResource(getResources().getIdentifier(
                            pEx.getPictureName(), "drawable", getPackageName()));
                    container.addView(img);
                } else {
                    me.setText("show image");
                    container.removeViewAt(container.getChildCount() - 1);
                }
            }
        });
        // Button container
        LinearLayout upperButtons = new LinearLayout(getApplicationContext());
        upperButtons.addView(up);
        upperButtons.addView(showImage);
        upperButtons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Down button
        Button down = new Button(getApplicationContext());
        down.setText("down");
        down.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout main = (LinearLayout) container.getParent();
                int max = main.getChildCount();
                int index = main.indexOfChild(container);
                if (index < max - 1) {
                    if (workout.moveDownExercise(index)) {
                        main.removeView(container);
                        main.addView(container, index + 1);
                    }
                }
            }
        });
        // Remove button
        Button remove = new Button(getApplicationContext());
        remove.setText("remove");
        remove.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout main = (LinearLayout) container.getParent();
                int idx = main.indexOfChild(container);
                if (workout.removeExercise(idx)) {
                    main.removeView(container);
                }
            }
        });
        // Button container
        LinearLayout lowerButtons = new LinearLayout(getApplicationContext());
        lowerButtons.addView(down);
        lowerButtons.addView(remove);
        lowerButtons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Add the components to the root layout
        container.addView(exName);
        container.addView(upperButtons);
        container.addView(lowerButtons);
        pExerciseDesc.addView(container);
    }
}