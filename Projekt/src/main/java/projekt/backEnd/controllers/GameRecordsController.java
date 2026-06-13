package projekt.backEnd.controllers;

import org.springframework.web.bind.annotation.*;
import projekt.backEnd.entities.GameRecord;
import projekt.backEnd.exceptions.GameRecordsException;
import projekt.backEnd.repositories.GameRecordRepository;

import java.util.List;

@RestController
public class GameRecordsController {
    private final GameRecordRepository gameRecordsRepo;

    GameRecordsController(GameRecordRepository gameRecordRepo){
        this.gameRecordsRepo = gameRecordRepo;
    }
    //get records by player
    @GetMapping("/gameRecords")
    public List<GameRecord> getGameRecords(@RequestParam Long playerId){
        if(playerId != null){
            return gameRecordsRepo.findByPlayerId(playerId);
        }
        return gameRecordsRepo.findAll();
    }

    //save game records for player
    @PostMapping("/gameRecords")
    GameRecord newGameRecords(@RequestBody GameRecord newGameRecords){
        return gameRecordsRepo.save(newGameRecords);
    }

    //return specific record for player by id
    @GetMapping("/gameRecords/{id}")
    GameRecord recordForPlayer(@PathVariable Long id){
        return gameRecordsRepo.findById(id).orElseThrow(() -> new GameRecordsException(id));
    }

    //update players game records
    @PutMapping("/gameRecords/{id}")
    GameRecord updateGameRecords(@RequestBody GameRecord newGameRecord, @PathVariable Long id){
        return gameRecordsRepo.findById(id)
                .map(gameRecord -> {
                    gameRecord.setTimestamp(newGameRecord.getTimestamp());
                    gameRecord.setTime(newGameRecord.getTime());
                    return gameRecordsRepo.save(gameRecord);
                })
                .orElseGet(() -> {
                    return gameRecordsRepo.save(newGameRecord);
                });
    }

    //delete game records of player
    @DeleteMapping("/gameRecords/{id}")
    void deleteGameRecords(@PathVariable Long id){
        gameRecordsRepo.deleteById(id);
    }
}
