package francescocristiano.CapstoneProject.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import francescocristiano.CapstoneProject.exceptions.NewErrorsDTO;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.exceptions.UnauthorizedException;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    public JWTAuthFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) throw new UnauthorizedException("Invalid token");
            String accessToken = header.substring(7);
            jwtTools.verifyToken(accessToken);
            String id = jwtTools.extractIdFrom(accessToken);
            User currentUser = userService.findById(UUID.fromString(id));
            Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            NewErrorsDTO errorDTO = new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
            String jsonResponse = objectMapper.writeValueAsString(errorDTO);
            response.getWriter().write(jsonResponse);
        } catch (NotFoundExpetion e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType("application/json");
            NewErrorsDTO errorDTO = new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
            String jsonResponse = objectMapper.writeValueAsString(errorDTO);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            NewErrorsDTO errorDTO = new NewErrorsDTO("Something went wrong with server, we are working on it", LocalDateTime.now());
            String jsonResponse = objectMapper.writeValueAsString(errorDTO);
            response.getWriter().write(jsonResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath()) ||
                new AntPathMatcher().match("/partecipations/**/reject", request.getServletPath()) ||
                new AntPathMatcher().match("/partecipations/**/reject", request.getServletPath());
    }
}
