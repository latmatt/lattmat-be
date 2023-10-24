package solution.com.lattmat.security.model;

import java.util.UUID;

public record UserInfoResponse(
        UUID id, String username, String firstName, String lastName, String phoneNumber, String mail, String profileImage
) {}
