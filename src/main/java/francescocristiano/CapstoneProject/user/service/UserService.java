package francescocristiano.CapstoneProject.user.service;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.partecipation.service.PartecipationService;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.team.service.TeamService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.admin.Admin;
import francescocristiano.CapstoneProject.user.repository.UserRepository;
import francescocristiano.CapstoneProject.user.userPayloads.CreateAdminDTO;
import francescocristiano.CapstoneProject.user.userPayloads.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    @Lazy
    private PartecipationService partecipationService;


    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }


    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }

    public Admin createAdmin(CreateAdminDTO adminDTO) {
        if (userRepository.findByEmail(adminDTO.email()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.findByUsername(adminDTO.username()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(adminDTO.password());
        Admin newAdmin = new Admin(adminDTO.name(), adminDTO.surname(), adminDTO.username(), adminDTO.email(), encodedPassword);
        return userRepository.save(newAdmin);
    }

    public User createUser(UserRegisterDTO userRegisterDTO, UUID teamId) {
        if (userRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.findByUsername(userRegisterDTO.username()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        switch (userRegisterDTO.userType().toUpperCase()) {
            case "COACH" -> {

                return userRepository.save(new Coach(userRegisterDTO.name(),
                        userRegisterDTO.surname(),
                        userRegisterDTO.username(),
                        bCryptPasswordEncoder.encode(userRegisterDTO.password()),
                        userRegisterDTO.email()));
            }
            case "PLAYER" -> {

                Player newPlayer = userRepository.save(new Player(userRegisterDTO.name(),
                        userRegisterDTO.surname(),
                        userRegisterDTO.username(),
                        bCryptPasswordEncoder.encode(userRegisterDTO.password()),
                        userRegisterDTO.email(),
                        teamId != null ? teamService.findById(teamId) : null));

                partecipationService.acceptPartecipationByEmail(newPlayer.getEmail());
                return newPlayer;
            }
            default -> {
                throw new BadRequestException("Invalid user type, must be COACH or PLAYER");
            }
        }
    }
}
