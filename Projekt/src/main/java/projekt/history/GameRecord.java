package projekt.history;

public class GameRecord implements Comparable<GameRecord> {
    private final String playerName;
    private final int level;
    private final long timeMs;
    private final long timestamp;

    public GameRecord(String playerName, int level, long timeMs, long timestamp) {
        this.playerName = playerName;
        this.level = level;
        this.timeMs = timeMs;
        this.timestamp = timestamp;
    }

    public String getPlayerName() {return playerName;}

    public int getLevel() {return level;}

    public long getTimeMs() {return timeMs;}

    public long getTimestamp() {return timestamp;}

    @Override
    public int compareTo(GameRecord other) {return Long.compare(this.timeMs, other.timeMs);}
}
