package ouch.ouchworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class OuchWorkout extends AppCompatActivity {
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_workout);

        try {
            InputStream is = getResources().openRawResource(R.raw.first);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            JSONObject myWorkout = new JSONObject(new String(buffer));
            workout = new Workout(this, myWorkout.getJSONArray("workout"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.play_pause) {
            if(workout.isRunning()){
                item.setIcon(android.R.drawable.ic_media_play);
                workout.stop();
            } else {
                item.setIcon(android.R.drawable.ic_media_pause);
                try {
                    workout.play();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
