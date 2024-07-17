package francescocristiano.CapstoneProject.location.trainingField;

import francescocristiano.CapstoneProject.location.Location;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrainingField extends Location {
    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    public TrainingField(String name, String address, String city, String country, FieldType fieldType) {
        super(name, address, city, country);
        this.fieldType = fieldType;
    }
}
