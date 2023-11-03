package solution.com.lattmat.entity.concert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ConcertTicketPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private double price;

    @OneToMany(mappedBy="concertTicketPlan")
    private List<ConcertSeat> concertSeatList;
}
