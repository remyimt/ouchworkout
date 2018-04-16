package ouch.ouchworkout.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Switch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import ouch.ouchworkout.R;
import ouch.ouchworkout.Settings;

public class SettingsAct extends AppCompatActivity {
    public static final String SETTINGS_FILE = "settings.json";
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouch_settings);
        // Get the settings
        settings = Settings.getSettings();
        // Set the current values for setting parameters
        Switch enableSound = findViewById(R.id.enable_sound_button);
        enableSound.setChecked(settings.isWithSound());
        EditText beepAt = findViewById(R.id.beep_at_seconds);
        beepAt.setText(String.valueOf(settings.getBeepTimeSeconds()));
        Switch enableManualSelection = findViewById(R.id.enable_manual_selection);
        enableManualSelection.setChecked(settings.isManualSelection());
    }

    private void saveSettings() {
        Switch enableSound = findViewById(R.id.enable_sound_button);
        settings.setWithSound(enableSound.isChecked());
        EditText beepAt = findViewById(R.id.beep_at_seconds);
        settings.setBeepTimeSeconds(Integer.valueOf(beepAt.getText().toString()));
        Switch enableManualSelection = findViewById(R.id.enable_manual_selection);
        settings.setManualSelection(enableManualSelection.isChecked());
        try {
            OutputStream file = openFileOutput(SETTINGS_FILE, MODE_PRIVATE);
            settings.saveSettings(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
