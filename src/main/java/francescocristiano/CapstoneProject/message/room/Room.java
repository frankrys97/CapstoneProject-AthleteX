package francescocristiano.CapstoneProject.message.room;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.CapstoneProject.message.Message;
import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"team", "messages"})
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

    public Room(String name, Team team) {
        this.name = name;
        this.team = team;
    }
}
