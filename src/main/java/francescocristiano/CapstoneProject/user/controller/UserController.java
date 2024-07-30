package francescocristiano.CapstoneProject.user.controller;

import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public User getUser(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }

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
