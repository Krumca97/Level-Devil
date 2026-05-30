package projekt.backEnd.exceptions;

public class PlayerException extends RuntimeException{
    public PlayerException (Long id){
        super("Player with id " + id + " not found");
    }
}
