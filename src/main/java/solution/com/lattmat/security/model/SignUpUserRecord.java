package solution.com.lattmat.security.model;

public record SignUpUserRecord(
        String firstName,
        String lastName,
        String username,
        String phoneNumber,
        String password,
        String mail,
        String profileImage
){}