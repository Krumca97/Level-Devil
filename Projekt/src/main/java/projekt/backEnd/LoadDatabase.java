package projekt.backEnd;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import projekt.backEnd.entities.Player;
import projekt.backEnd.entities.PlayerSettings;
import projekt.backEnd.repositories.GameRecordRepository;
import projekt.backEnd.repositories.LevelsProgressRepository;
import projekt.backEnd.repositories.PlayerRepository;
import org.slf4j.Logger;
import projekt.backEnd.repositories.PlayerSettingsRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PlayerRepository playerRepo, PlayerSettingsRepository playerSettingsRepo, GameRecordRepository gameRecordRepo, LevelsProgressRepository levelsProgressRepo) {
        return args -> {
            log.info("Preloading " + playerRepo.save(new Player()));
            log.info("Preloading " + playerSettingsRepo.save(new PlayerSettings()));
            log.info("Preloading " + gameRecordRepo.save(new projekt.backEnd.entities.GameRecord()));
            log.info("Preloading " + levelsProgressRepo.save(new projekt.backEnd.entities.LevelsProgress()));
        };
    }

}
