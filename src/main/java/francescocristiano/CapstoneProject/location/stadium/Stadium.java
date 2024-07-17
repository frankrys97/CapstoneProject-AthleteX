package francescocristiano.CapstoneProject.location.stadium;

import francescocristiano.CapstoneProject.location.Location;
import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stadium extends Location {
    private int capacity;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Stadium(String name, String address, String city, String country, int capacity) {
        super(name, address, city, country);
        this.capacity = capacity;
    }
}
