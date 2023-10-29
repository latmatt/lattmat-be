package solution.com.lattmat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import solution.com.lattmat.domain.CustomResponse;

public class BaseController {

    protected <T>ResponseEntity<CustomResponse<T>> createResponse(
            boolean isSuccess, HttpStatus httpStatus, T data, String message
    ){
        return createResponse(isSuccess, httpStatus,null, data, message);
    }

    protected <T> ResponseEntity<CustomResponse<T>> createResponse(
            boolean isSuccess, HttpStatus httpStatus, MultiValueMap<String, String> headers, T data, String message
    ){
        CustomResponse response = CustomResponse.builder()
                .success(isSuccess).code(httpStatus.value()).data(data).message(message).build();
        return new ResponseEntity<>(response, headers, httpStatus);
    }

}
