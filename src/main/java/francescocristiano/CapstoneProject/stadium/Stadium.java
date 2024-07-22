package francescocristiano.CapstoneProject.stadium;

import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stadium {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String city;
    private String address;
    private long capacity;

    @OneToOne(mappedBy = "stadium")
    private Team team;

    public Stadium(String name, String city, String address, long capacity, Team team) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.capacity = capacity;
        this.team = team;
    }
}
