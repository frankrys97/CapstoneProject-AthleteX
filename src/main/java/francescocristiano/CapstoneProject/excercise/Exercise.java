package francescocristiano.CapstoneProject.excercise;

import francescocristiano.CapstoneProject.event.training.TrainingType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private String videoUrl;
    @ManyToMany
    private List<TrainingType> supportedTrainingTypes;

    public Exercise(String name, String description, String videoUrl, List<TrainingType> supportedTrainingTypes) {
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
        this.supportedTrainingTypes = supportedTrainingTypes;
    }
}
