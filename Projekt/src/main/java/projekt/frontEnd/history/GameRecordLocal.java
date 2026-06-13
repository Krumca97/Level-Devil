package projekt.frontEnd.history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Setter
public class GameRecordLocal implements Comparable<GameRecordLocal> {
    private final String playerName;
    private final int level;
    private final long timeMs;
    private final long timestamp;

    @Override
    public int compareTo(GameRecordLocal other) {return Long.compare(this.timeMs, other.timeMs);}
}
