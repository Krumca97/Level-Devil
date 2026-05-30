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
public class PlayerSettings {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    private String difficulty;
    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
