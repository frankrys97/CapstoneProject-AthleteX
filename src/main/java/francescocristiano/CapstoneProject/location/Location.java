package francescocristiano.CapstoneProject.location;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Location {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String avatar;

    public Location(String name, String address, String city, String country) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.avatar = "https://ui-avatars.com/api/?name=" + name;
    }
}
