package solution.com.lattmat.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.exception.domain.InvalidCredentialsException;
import solution.com.lattmat.exception.domain.PhoneNumberAlreadyExistException;
import solution.com.lattmat.exception.domain.TokenRefreshException;
import solution.com.lattmat.exception.domain.UsernameAlreadyExistException;

import static org.springframework.http.HttpStatus.*;
import static solution.com.lattmat.constant.MessageConstant.*;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<CustomResponse> usernameAlreadyExistException() {
        return createHttpResponse(CONFLICT, USERNAME_ALREADY_EXIST);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistException.class)
    public ResponseEntity<CustomResponse> phoneNumberAlreadyExistException() {
        return createHttpResponse(CONFLICT, PHONE_NUMBER_ALREADY_EXIST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomResponse> invalidCredentialsException(Exception ex) {
        return createHttpResponse(FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CustomResponse> invalidCredentialsException() {
        return createHttpResponse(FORBIDDEN, INVALID_CREDENTIALS);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<CustomResponse> tokenRefreshException(Exception ex) {
        return createHttpResponse(FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomResponse> expiredJwtException(Exception ex) {
        return createHttpResponse(FORBIDDEN, "JWT Token is expired.");
    }

    private ResponseEntity<CustomResponse> createHttpResponse(HttpStatus httpStatus, String message){
        CustomResponse response = CustomResponse.builder()
                .success(false).code(httpStatus.value()).data(null).message(message).build();
        return new ResponseEntity<>(response, httpStatus);
    }

}
