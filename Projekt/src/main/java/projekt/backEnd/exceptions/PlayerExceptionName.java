package projekt.backEnd.exceptions;

public class PlayerExceptionName extends RuntimeException{
    public PlayerExceptionName(String name){
        super("Player with name " + name + " not found");
    }
}
