package francescocristiano.CapstoneProject.coach;

import francescocristiano.CapstoneProject.message.Message;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.UserRole;
import francescocristiano.CapstoneProject.user.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coach extends User {
    @OneToMany(mappedBy = "coach")
    private List<Team> teamCoached;
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;
    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    public Coach(String nome, String cognome, String username, String password, String email) {
        super(nome, cognome, username, password, email, UserRole.ADMIN, UserType.COACH);
    }
}
