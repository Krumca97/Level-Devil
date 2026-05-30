package projekt.backEnd.controllers;

import org.springframework.web.bind.annotation.*;
import projekt.backEnd.entities.GameRecord;
import projekt.backEnd.repositories.GameRecordRepository;

@RestController
public class GameRecordsController {
    private final GameRecordRepository gameRecordsRepo;

    GameRecordsController(GameRecordRepository gameRecordRepo){
        this.gameRecordsRepo = gameRecordRepo;
    }

    //get all game records for player
    @GetMapping("/gameRecords")
    public Object getAllGameRecords(){
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
        return gameRecordsRepo.findById(id).orElseThrow();
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
