package ouch.ouchworkout;

import android.app.Activity;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import ouch.ouchworkout.exception.NoExternalDirectoryException;

public class Settings {
    private final String WITH_SOUND_KEY = "with_sound";
    private final String BEEP_TIME_SECONDS_KEY = "beep_time_seconds";
    private final String MANUAL_EXERCISE_SELECTION = "manual_exercise_selection";
    private boolean withSound = false;
    private boolean manualSelection = false;
    private int beepTimeSeconds = 1;

    protected Settings() {
    }

    protected Settings(JSONObject pConfig) throws JSONException {
        withSound = pConfig.getBoolean(WITH_SOUND_KEY);
        beepTimeSeconds = pConfig.getInt(BEEP_TIME_SECONDS_KEY);
        manualSelection = pConfig.getBoolean(MANUAL_EXERCISE_SELECTION);
    }

    public boolean isWithSound() {
        return withSound;
    }

    public boolean isManualSelection() {
        return manualSelection;
    }

    public int getBeepTimeSeconds() {
        return beepTimeSeconds;
    }

    public File getExternalDirectory(Activity pAct) throws NoExternalDirectoryException {
        File f = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "Workouts");
        if (f.exists()) {
            // Create JSON files in the Downloads directory
            return f;
        } else {
            if (f.mkdirs()) {
                // Create JSON files in the Downloads directory
                return f;
            } else {
                // Try to write in the application directory
                f = new File(pAct.getApplicationContext().getFilesDir(), "Workouts");
                if (f.exists()) {
                    // Create JSON files in the application directory (internal storage)
                    return f;
                }
                if (f.mkdirs()) {
                    // Create JSON files in the application directory (internal storage)
                    return f;
                } else {
                    // Do not save JSON files
                    throw new NoExternalDirectoryException();
                }
            }
        }
    }

    public void setBeepTimeSeconds(int pBeepTime) {
        beepTimeSeconds = pBeepTime;
    }

    public void setWithSound(boolean pSound) {
        withSound = pSound;
    }

    public void setManualSelection(boolean pManualSelection) {
        manualSelection = pManualSelection;
    }

    public void saveSettings(OutputStream pFile) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\n");
        buffer.append("  \"" + WITH_SOUND_KEY + "\": " + withSound + ",\n");
        buffer.append("  \"" + BEEP_TIME_SECONDS_KEY + "\": " + beepTimeSeconds + ",\n");
        buffer.append("  \"" + MANUAL_EXERCISE_SELECTION + "\": " + manualSelection + "\n");
        buffer.append("}\n");
        try {
            pFile.write(buffer.toString().getBytes());
            pFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
