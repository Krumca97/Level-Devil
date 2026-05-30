package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
