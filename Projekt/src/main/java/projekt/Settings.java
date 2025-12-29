package projekt;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Settings {

    private static final String FILE_NAME = "settings.csv";

    private Map<String, String> data = new HashMap<>();

    public String get(String key, String defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public void save() throws SettingsException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (var entry : data.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }

        } catch (IOException e) {
            throw new SettingsException("Failed to save: " + e.getMessage());
        }
    }

    public void load() throws SettingsException {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            throw new SettingsException("File doesnt exist");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) {
                    lineNumber++;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length != 2) {
                    throw new SettingsException("bad line format" + lineNumber);
                }

                data.put(parts[0].trim(), parts[1].trim());
                lineNumber++;
            }

        } catch (IOException e) {
            throw new SettingsException("Failed to load" + e.getMessage());
        }
    }
}
