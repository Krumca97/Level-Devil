package projekt.backEnd.exceptions;

import projekt.backEnd.entities.PlayerSettings;

public class PlayerSettingsException extends RuntimeException{
    public PlayerSettingsException (Long id){
        super("Settings with id " + id + " not found for this player");
    }
}
