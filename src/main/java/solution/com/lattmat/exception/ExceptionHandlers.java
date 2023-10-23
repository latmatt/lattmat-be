package solution.com.lattmat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import solution.com.lattmat.domain.CustomResponse;
import solution.com.lattmat.exception.domain.UsernameAlreadyExistException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static solution.com.lattmat.constant.MessageConstant.USERNAME_ALREADY_EXIST;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<CustomResponse> usernameAlreadyExistException() {
        return createHttpResponse(CONFLICT, USERNAME_ALREADY_EXIST);
    }

    private ResponseEntity<CustomResponse> createHttpResponse(HttpStatus httpStatus, String message){
        CustomResponse response = CustomResponse.builder()
                .success(false).code(httpStatus.value()).data(null).message(message).build();
        return new ResponseEntity<>(response, httpStatus);
    }

}
