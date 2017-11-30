package ouch.ouchworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

public class OuchSettings extends AppCompatActivity {
    public static boolean WITH_SOUND = false;
    public static int BEEP_TIME_SECONDS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_settings);
        // Set the current values for setting parameters
        Switch enableSound = (Switch) findViewById(R.id.enable_sound_button);
        enableSound.setChecked(WITH_SOUND);
        EditText beepAt = (EditText) findViewById(R.id.beep_at_seconds);
        beepAt.setText(String.valueOf(BEEP_TIME_SECONDS));
    }

    private void saveSettings(){
        Switch enableSound = (Switch) findViewById(R.id.enable_sound_button);
        WITH_SOUND = enableSound.isChecked();
        EditText beepAt = (EditText) findViewById(R.id.beep_at_seconds);
        BEEP_TIME_SECONDS = Integer.valueOf(beepAt.getText().toString());
    }

    @Override
    public void onBackPressed() {
        saveSettings();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        saveSettings();
        return super.onSupportNavigateUp();
    }
}
