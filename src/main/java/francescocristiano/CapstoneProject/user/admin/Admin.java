package francescocristiano.CapstoneProject.user.admin;

import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.enums.UserRole;
import francescocristiano.CapstoneProject.user.enums.UserType;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    public Admin(String nome, String cognome, String username, String email, String password) {
        super(nome, cognome, username, password, email, UserType.ADMIN, UserRole.ADMIN);
    }

}
