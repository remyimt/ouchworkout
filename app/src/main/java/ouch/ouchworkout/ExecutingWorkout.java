package ouch.ouchworkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

public class ExecutingWorkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executing_workout);
        Workout.getWorkout().initializeWorkout(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Workout w = Workout.getWorkout();
        if (item.getItemId() == R.id.play_pause) {
            if (w.isRunning()) {
                item.setIcon(android.R.drawable.ic_media_play);
            } else {
                item.setIcon(android.R.drawable.ic_media_pause);
            }
            w.playPause();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
