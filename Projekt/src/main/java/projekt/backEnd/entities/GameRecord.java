package projekt.backEnd.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GameRecord {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private long timestamp;
    private long time;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
