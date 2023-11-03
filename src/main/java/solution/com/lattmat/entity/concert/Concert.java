package solution.com.lattmat.entity.concert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import solution.com.lattmat.entity.Event;
import solution.com.lattmat.entity.Location;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private String coverImage;

    @OneToMany(targetEntity = Location.class)
    private Set<Location> locations;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    private Event event;

}
