package francescocristiano.CapstoneProject.location.stadium;

import francescocristiano.CapstoneProject.location.Location;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stadium extends Location {
    private int capacity;

    public Stadium(String name, String address, String city, String country, int capacity) {
        super(name, address, city, country);
        this.capacity = capacity;
    }
}
