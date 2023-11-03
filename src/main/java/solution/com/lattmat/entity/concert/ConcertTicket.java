package solution.com.lattmat.entity.concert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import solution.com.lattmat.entity.Users;

import java.util.UUID;

@Entity
@Getter
@Setter
public class ConcertTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "concert_seat_id", referencedColumnName = "id")
    private ConcertSeat seat;

    @ManyToOne
    @JoinColumn(name="concert_id", nullable=false)
    private Concert concert;

    @ManyToOne
    @JoinColumn(name="concert_ticket_plan", nullable=false)
    private ConcertTicketPlan concertTicketPlan;


}
