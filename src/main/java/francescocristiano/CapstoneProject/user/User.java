package francescocristiano.CapstoneProject.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private UserRole role;
    private UserType userType;

    public User(String username, String password, String email, String avatar, UserRole role, UserType userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.role = role;
        this.userType = userType;
    }
}
