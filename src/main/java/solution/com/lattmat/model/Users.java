package solution.com.lattmat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String password;
    private String mail;
    private String profileImage;
    private boolean isActive;
    private boolean isLock;
}
