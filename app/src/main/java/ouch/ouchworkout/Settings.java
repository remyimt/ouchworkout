package ouch.ouchworkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Settings {
    private static Settings instance = null;
    private final String WITH_SOUND_KEY = "with_sound";
    private final String BEEP_TIME_SECONDS_KEY = "beep_time_seconds";
    private boolean withSound = false;
    private int beepTimeSeconds = 1;

    private Settings() {
    }

    private Settings(JSONObject pConfig) throws JSONException {
        withSound = pConfig.getBoolean(WITH_SOUND_KEY);
        beepTimeSeconds = pConfig.getInt(BEEP_TIME_SECONDS_KEY);
    }

    public boolean isWithSound() {
        return withSound;
    }

    public int getBeepTimeSeconds() {
        return beepTimeSeconds;
    }

    public static Settings getSettings() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public void setBeepTimeSeconds(int pBeepTime) {
        beepTimeSeconds = pBeepTime;
    }

    public void setWithSound(boolean pSound) {
        withSound = pSound;
    }

    public static void loadSettings(InputStream pFile) {
        if (instance == null) {
            try {
                byte[] buffer = new byte[pFile.available()];
                pFile.read(buffer);
                pFile.close();
                JSONObject config = new JSONObject(new String(buffer));
                instance = new Settings(config);
            } catch (Exception e) {
                e.printStackTrace();
                instance = new Settings();
            }
        }
    }

    public void saveSettings(OutputStream pFile) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\n");
        buffer.append("  \"" + WITH_SOUND_KEY + "\": " + withSound+",\n");
        buffer.append("  \"" + BEEP_TIME_SECONDS_KEY + "\": " + beepTimeSeconds+"\n");
        buffer.append("}\n");
        try {
            pFile.write(buffer.toString().getBytes());
            pFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
