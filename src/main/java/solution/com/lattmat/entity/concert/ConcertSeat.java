package solution.com.lattmat.entity.concert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="concert_ticket_plan_id", nullable=false)
    private ConcertTicketPlan concertTicketPlan;
}
