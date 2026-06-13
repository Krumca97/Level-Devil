package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.PlayerSettings;

import java.util.List;

public interface PlayerSettingsRepository extends JpaRepository<PlayerSettings, Long> {
    List<PlayerSettings> findByPlayerId(Long playerId);
}
