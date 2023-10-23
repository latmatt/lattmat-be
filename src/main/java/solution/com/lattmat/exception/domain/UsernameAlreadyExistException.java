package solution.com.lattmat.exception.domain;

public class UsernameAlreadyExistException extends RuntimeException{
    public UsernameAlreadyExistException(String msg){
        super(msg);
    }
}
