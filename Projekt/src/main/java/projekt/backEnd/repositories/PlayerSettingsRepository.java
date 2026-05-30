package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.PlayerSettings;

public interface PlayerSettingsRepository extends JpaRepository<PlayerSettings, Long> {
}
