package francescocristiano.CapstoneProject.user.controller;

import francescocristiano.CapstoneProject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint per verificare la disponibilità dell'username
    @GetMapping("/check-username/{username}")
    public boolean checkUsernameAvailability(@PathVariable String username) {
        return userService.isUsernameTaken(username);
    }

    // Endpoint per verificare la disponibilità dell'email
    @GetMapping("/check-email/{email}")
    public boolean checkEmailAvailability(@PathVariable String email) {
        return userService.isEmailTaken(email);
    }
}
