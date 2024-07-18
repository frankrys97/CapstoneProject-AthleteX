package francescocristiano.CapstoneProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NewErrorsDTO handleBadRequest(BadRequestException e) {
        return new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundExpetion.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NewErrorsDTO handleNotFound(NotFoundExpetion e) {
        return new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public NewErrorsDTO handleUnauthorized(UnauthorizedException e) {
        return new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public NewErrorsDTO handleException(Exception e) {
        return new NewErrorsDTO("Something went wrong with server, we are working on it", LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public NewErrorsDTO handleAuthorization(AuthorizationDeniedException e) {
        return new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public NewErrorsDTO handleForbidden(ForbiddenException e) {
        return new NewErrorsDTO(e.getMessage(), LocalDateTime.now());
    }
}
