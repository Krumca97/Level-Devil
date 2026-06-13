package projekt.backEnd.controllers;

import org.springframework.web.bind.annotation.*;
import projekt.backEnd.entities.LevelsProgress;
import projekt.backEnd.exceptions.LevelProgressException;
import projekt.backEnd.repositories.LevelsProgressRepository;

import java.util.List;

@RestController
public class LevelsProgressController {
    private final LevelsProgressRepository levelsProgressRepo;

    LevelsProgressController(LevelsProgressRepository levelsProgressRepo){
        this.levelsProgressRepo = levelsProgressRepo; 
    }

    //get level progress by player
    @GetMapping("/levelProgress")
    public List<LevelsProgress> getLevelProgress(@RequestParam Long playerId){
        if(playerId != null){
            return levelsProgressRepo.findByPlayerId(playerId);
        }
        return levelsProgressRepo.findAll();
    }
    //save level progress
    @PostMapping("/levelProgress")
    LevelsProgress newLevelProgress(@RequestBody LevelsProgress newLevelProgress) {
        return levelsProgressRepo.save(newLevelProgress);
    }

    // return specific progres of level
    @GetMapping("/levelProgress/{id}")
    LevelsProgress firstLevelProgress(@PathVariable Long id){
        return levelsProgressRepo.findById(id).orElseThrow(() -> new LevelProgressException(id));
    }

    //update specific progress of level
    @PutMapping("/levelProgress/{id}")
    LevelsProgress replaceLevelProgress(@RequestBody LevelsProgress newLevelProgress,@PathVariable Long id) {
        return levelsProgressRepo.findById(id)
                .map(levelsProgress -> {
                    levelsProgress.setLevel1Completed(newLevelProgress.isLevel1Completed());
                    levelsProgress.setLevel2Completed(newLevelProgress.isLevel2Completed());
                    levelsProgress.setLevel3Completed(newLevelProgress.isLevel3Completed());
                    return levelsProgressRepo.save(levelsProgress);
                })
                .orElseGet(() -> {
                    return levelsProgressRepo.save(newLevelProgress);
                });
    }

     //delete specific progress of level
     @DeleteMapping("/levelProgress/{id}")
     void deleteLevelProgress(@PathVariable Long id){
         levelsProgressRepo.deleteById(id);
     }
}
