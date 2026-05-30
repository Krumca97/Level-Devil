package projekt.backEnd.controllers;

import org.springframework.web.bind.annotation.*;
import projekt.backEnd.entities.Player;
import projekt.backEnd.repositories.PlayerRepository;
import projekt.backEnd.exceptions.PlayerException;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerRepository playerRepo;

    PlayerController(PlayerRepository playerRepo){
        this.playerRepo = playerRepo;
    }

    //get all players
    @GetMapping("/players")
    public List<Player> getAllPlayers(){
        return playerRepo.findAll();
    }

    //save player
    @PostMapping("/players")
    Player newPlayer(@RequestBody Player newPlayer){
        return playerRepo.save(newPlayer);
    }

    // return specific player by ID
    @GetMapping("/players/{id}")
    Player firstPlayer(@PathVariable Long id){
        return playerRepo.findById(id).orElseThrow(() -> new PlayerException(id));
    }

    //update player
    @PutMapping("/players/{id}")
    Player replacePlayer(@RequestBody Player newPlayer,@PathVariable Long id) {
        return playerRepo.findById(id)
                .map(player -> {
                    player.setName(newPlayer.getName());
                    return playerRepo.save(player);
                })
                .orElseGet(() -> {
                    return playerRepo.save(newPlayer);
                });
    }

    //delete player
    @DeleteMapping("/players/{id}")
    void deletePlayer(@PathVariable Long id){
        playerRepo.deleteById(id);
    }
}
