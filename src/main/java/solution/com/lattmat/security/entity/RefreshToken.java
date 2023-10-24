package solution.com.lattmat.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import solution.com.lattmat.model.Users;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
}
