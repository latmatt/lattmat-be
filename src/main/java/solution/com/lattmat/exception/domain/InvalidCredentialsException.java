package solution.com.lattmat.exception.domain;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String msg){
        super(msg);
    }

}
