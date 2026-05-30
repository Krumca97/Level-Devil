package projekt.backEnd.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LevelsProgress {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private boolean level1Completed;
    private boolean level2Completed;
    private boolean level3Completed;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
