package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.LevelsProgress;

public interface LevelsProgressRepository extends JpaRepository<LevelsProgress, Long> {
}
