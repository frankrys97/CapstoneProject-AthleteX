package francescocristiano.CapstoneProject.team.formation;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.event.match.Match;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Formation {

    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id")
    private Match match;

    @OneToMany(mappedBy = "formation")
    private List<PlayerFormation> players;

    @Enumerated(EnumType.STRING)
    private FormationType formationType;

    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Coach createdBy;

    public Formation(Match match, FormationType formationType, LocalDateTime dateCreated, Coach createdBy) {
        this.match = match;
        this.formationType = formationType;
        this.dateCreated = LocalDateTime.now();
        this.createdBy = createdBy;
    }
}
