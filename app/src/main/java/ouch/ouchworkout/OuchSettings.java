package ouch.ouchworkout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;

import java.io.FileNotFoundException;

public class OuchSettings extends AppCompatActivity {
    public static final String SETTINGS_FILE = "settings.json";
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_settings);
        // Get the settings
        settings = Settings.getSettings();
        // Set the current values for setting parameters
        Switch enableSound = (Switch) findViewById(R.id.enable_sound_button);
        enableSound.setChecked(settings.isWithSound());
        EditText beepAt = (EditText) findViewById(R.id.beep_at_seconds);
        beepAt.setText(String.valueOf(settings.getBeepTimeSeconds()));
    }

    private void saveSettings() {
        Switch enableSound = (Switch) findViewById(R.id.enable_sound_button);
        settings.setWithSound(enableSound.isChecked());
        EditText beepAt = (EditText) findViewById(R.id.beep_at_seconds);
        settings.setBeepTimeSeconds(Integer.valueOf(beepAt.getText().toString()));
        try {
            settings.saveSettings(openFileOutput(SETTINGS_FILE, MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
