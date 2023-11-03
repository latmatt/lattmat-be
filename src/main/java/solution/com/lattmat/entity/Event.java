package solution.com.lattmat.entity;

import jakarta.persistence.*;
import lombok.*;
import solution.com.lattmat.entity.concert.Concert;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_type_id", nullable=false)
    private EventType eventType;

    @OneToMany(mappedBy="event")
    private List<Concert> concert;

}
