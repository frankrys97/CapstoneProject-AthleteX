package francescocristiano.CapstoneProject.location.gym;

import francescocristiano.CapstoneProject.location.Location;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Gym extends Location {

    public Gym(String name, String address, String city, String country) {
        super(name, address, city, country);
    }
}
