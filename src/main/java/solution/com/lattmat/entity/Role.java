package solution.com.lattmat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solution.com.lattmat.enumeration.UserRoleType;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserRoleType roleType;

    @ManyToMany(mappedBy = "roles")
    private Set<Users> userList;

    public Role(UserRoleType roleType){
        this.roleType = roleType;
    }
}
