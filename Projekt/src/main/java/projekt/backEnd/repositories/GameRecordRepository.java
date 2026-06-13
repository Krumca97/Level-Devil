package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.GameRecord;

import java.util.List;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
    List<GameRecord> findByPlayerId(Long playerId);
}
