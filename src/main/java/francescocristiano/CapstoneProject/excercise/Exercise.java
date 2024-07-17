package francescocristiano.CapstoneProject.excercise;

import francescocristiano.CapstoneProject.event.training.Training;
import francescocristiano.CapstoneProject.event.training.TrainingType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private String videoUrl;

    @ManyToMany(mappedBy = "exercises")
    private List<Training> training;
    @ElementCollection(targetClass = TrainingType.class)
    @CollectionTable(name = "exercise_supported_training_types", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    private List<TrainingType> supportedTrainingTypes;

    public Exercise(String name, String description, String videoUrl, List<TrainingType> supportedTrainingTypes) {
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
        this.supportedTrainingTypes = supportedTrainingTypes;
    }
}
