package projekt.backEnd.exceptions;

public class LevelProgressException extends RuntimeException{
    public LevelProgressException(Long id){super("Level progress with id " + id + " not found");}
}
