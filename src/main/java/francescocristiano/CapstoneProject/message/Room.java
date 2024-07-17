package francescocristiano.CapstoneProject.message;

import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "room")
    private List<Message> messages;
}
