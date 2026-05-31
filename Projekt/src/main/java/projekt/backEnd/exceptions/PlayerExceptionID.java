package projekt.backEnd.exceptions;

public class PlayerExceptionID extends RuntimeException{
    public PlayerExceptionID(Long id){
        super("Player with id " + id + " not found");
    }

}
