package projekt.history;

import java.io.*;
import java.util.*;

public class HistoryManager {
    private static final String FILE = "history.txt";
    private static final int MAX_RECORDS = 10;

    public static List<GameRecord> load() {
        List<GameRecord> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                list.add(new GameRecord(p[0], Integer.parseInt(p[1]), Long.parseLong(p[2]), Long.parseLong(p[3])));
            }
        } catch (IOException ignored) {}
        return list;
    }

    public static void saveRecord(GameRecord record) {
        List<GameRecord> list = load();
        list.add(record);

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (GameRecord r : list) {
                pw.println(r.getPlayerName() + ";" + r.getLevel() + ";" + r.getTimeMs() + ";" + r.getTimestamp());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
