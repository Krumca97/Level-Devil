package projekt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class SaveManager {
    private static final Path SAVE_FILE =
            Path.of(System.getProperty("user.home"), ".leveldevil_save.properties");

    public static void saveCompletedLevels(Set<Integer> completedLevels) {
        try {Properties props = new Properties();

            String value = completedLevels.stream().map(String::valueOf).collect(Collectors.joining(","));
            props.setProperty("completedLevels", value);

            try (FileOutputStream out = new FileOutputStream(SAVE_FILE.toFile())) {
                props.store(out, "Level Devil Save");
            }

            System.out.println("SAVE OK → " + SAVE_FILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCompletedLevels(Set<Integer> completedLevels) {
        try {
            if (!Files.exists(SAVE_FILE)) {
                System.out.println("NO SAVE FOUND");
                return;
            }

            Properties props = new Properties();

            try (FileInputStream in = new FileInputStream(SAVE_FILE.toFile())) {
                props.load(in);
            }

            String value = props.getProperty("completedLevels", "");
            if (value.isEmpty()) {
                return;
            }

            for (String s : value.split(",")) {
                completedLevels.add(Integer.parseInt(s.trim()));
            }

            System.out.println("LOAD OK → " + completedLevels);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
