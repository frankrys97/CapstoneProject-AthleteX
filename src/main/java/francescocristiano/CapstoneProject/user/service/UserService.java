package francescocristiano.CapstoneProject.user.service;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.team.service.TeamService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.repository.UserRepository;
import francescocristiano.CapstoneProject.user.userPayloads.UserRegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
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


    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundExpetion("User not found"));
    }

    public User createUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
            throw new NotFoundExpetion("Email already exists");
        }
        if (userRepository.findByUsername(userRegisterDTO.username()).isPresent()) {
            throw new NotFoundExpetion("Username already exists");
        }

        switch (userRegisterDTO.userType()) {
            case "coach" -> {
                return userRepository.save(new Coach(userRegisterDTO.name(),
                        userRegisterDTO.surname(),
                        userRegisterDTO.username(),
                        bCryptPasswordEncoder.encode(userRegisterDTO.password()),
                        userRegisterDTO.email()));
            }
            case "player" -> {
                if (userRegisterDTO.teamId() == null) {
                    throw new NotFoundExpetion("Team not found");
                }
                return userRepository.save(new Player(userRegisterDTO.name(),
                        userRegisterDTO.surname(),
                        userRegisterDTO.username(),
                        bCryptPasswordEncoder.encode(userRegisterDTO.password()),
                        userRegisterDTO.email(),
                        teamService.findById(userRegisterDTO.teamId())));
            }
            default -> {
                throw new NotFoundExpetion("Invalid user type");
            }
        }
    }
}
