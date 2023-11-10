package solution.com.lattmat.exception.domain;

public class OTPExpiredException extends RuntimeException{

    public OTPExpiredException(String msg){
        super(msg);
    }

}
