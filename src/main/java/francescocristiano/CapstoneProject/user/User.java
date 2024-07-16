package francescocristiano.CapstoneProject.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private String email;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(String nome, String cognome, String username, String password, String email, UserRole role, UserType userType) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
        this.role = role;
        this.userType = userType;
    }
}
