package solution.com.lattmat.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse <T> {
    private boolean success;
    private int code;
    private T data;
    private String message;
}