package ouch.ouchworkout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class OuchWorkout extends AppCompatActivity {
    private Map<String, Integer> name2id = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_workout);

        Field[] fields = R.raw.class.getFields();
        for (Field f : fields) {
            try {
                if (f.getName().endsWith("_wo")) {
                    InputStream is = getResources().openRawResource(f.getInt(f));
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    is.close();
                    JSONObject myWorkout = new JSONObject(new String(buffer));
                    name2id.put(myWorkout.getString("name"), f.getInt(f));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (final String s : name2id.keySet()) {
            Button b = new Button(this);
            b.setText(s);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ExecutingWorkout.class);
                    intent.putExtra("workout.id", name2id.get(s));
                    intent.putExtra("workout.name", s);
                    startActivity(intent);
                }
            });
            LinearLayout layout = (LinearLayout) findViewById(R.id.workout_list);
            layout.addView(b);
        }
    }

    public void selectWorkout(View pView) {
    }
}
