package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.LevelsProgress;

import java.util.List;

public interface LevelsProgressRepository extends JpaRepository<LevelsProgress, Long> {
    List<LevelsProgress> findByPlayerId(Long playerId);
}
