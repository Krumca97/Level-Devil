package projekt.backEnd.exceptions;

public class GameRecordsException extends RuntimeException{
    public GameRecordsException(Long id){super("Game record with id " + id + " not found");}
}
