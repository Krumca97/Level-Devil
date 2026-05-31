package projekt.backEnd.controllers;

import org.springframework.web.bind.annotation.*;
import projekt.backEnd.entities.PlayerSettings;
import projekt.backEnd.exceptions.PlayerSettingsException;
import projekt.backEnd.repositories.PlayerSettingsRepository;

import java.util.List;

@RestController
public class PlayerSettingsController {

    private final PlayerSettingsRepository playerSettingsRepo;

    PlayerSettingsController(PlayerSettingsRepository playerSettingsRepo){
        this.playerSettingsRepo = playerSettingsRepo;
    }

    //get all settings from player
    @GetMapping("/playerSettings")
    public List<PlayerSettings> getAllPlayerSettings(){
        return playerSettingsRepo.findAll();
    }

    //save player settings
    @PostMapping("/playerSettings")
    PlayerSettings newPlayerSettings(@RequestBody PlayerSettings newPlayerSettings){
        return playerSettingsRepo.save(newPlayerSettings);
    }

    // return specific player settings by ID
    @GetMapping("/playerSettings/{id}")
    PlayerSettings firstPlayer(@PathVariable Long id){
        return playerSettingsRepo.findById(id).orElseThrow(() -> new PlayerSettingsException(id));
    }

    //update player settings
    @PutMapping("/playerSettings/{id}")
    PlayerSettings replacePlayer(@RequestBody PlayerSettings newPlayerSettings,@PathVariable Long id) {
        return playerSettingsRepo.findById(id)
                .map(playerSettings -> {
                    playerSettings.setDifficulty(newPlayerSettings.getDifficulty());
                    return playerSettingsRepo.save(playerSettings);
                })
                .orElseGet(() -> {
                    return playerSettingsRepo.save(newPlayerSettings);
                });
    }

    //delete player setting
    @DeleteMapping("/playerSettings/{id}")
    void deletePlayerSettings(@PathVariable Long id){
        playerSettingsRepo.deleteById(id);
    }
}
