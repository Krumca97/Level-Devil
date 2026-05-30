package projekt.backEnd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt.backEnd.entities.GameRecord;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
}
