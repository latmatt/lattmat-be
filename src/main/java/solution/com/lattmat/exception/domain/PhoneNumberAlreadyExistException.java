package solution.com.lattmat.exception.domain;

public class PhoneNumberAlreadyExistException extends RuntimeException{
    public PhoneNumberAlreadyExistException(String msg){
        super(msg);
    }
}
