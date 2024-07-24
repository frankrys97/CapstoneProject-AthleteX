package francescocristiano.CapstoneProject.security.auth;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.UnauthorizedException;
import francescocristiano.CapstoneProject.security.JWTTools;
import francescocristiano.CapstoneProject.security.TokenInvalidateService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.service.UserService;
import francescocristiano.CapstoneProject.user.userPayloads.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenInvalidateService tokenInvalidateService;


    public String authenticateAndGenerateToken(UserLoginDTO userLoginDTO) {

        User user = userService.findByEmail(userLoginDTO.email());
        if (bCryptPasswordEncoder.matches(userLoginDTO.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Invalid Authorization header");
        }
        String token = authorizationHeader.substring(7);
        tokenInvalidateService.invalidateToken(token);
    }
}

