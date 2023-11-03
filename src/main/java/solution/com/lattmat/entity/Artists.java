package solution.com.lattmat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Artists {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String profileImage;
    private String description;
    private LocalDate dob;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;

}
