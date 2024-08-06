package francescocristiano.CapstoneProject.partecipation.controller;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.partecipation.NewPartecipationDTO;
import francescocristiano.CapstoneProject.partecipation.Partecipation;
import francescocristiano.CapstoneProject.partecipation.service.PartecipationService;
import francescocristiano.CapstoneProject.user.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/partecipations")
public class PartecipationController {

    @Autowired
    private PartecipationService partecipationService;

    @GetMapping("/{teamId}")
    public Page<Partecipation> getAll(@PathVariable UUID teamId, @AuthenticationPrincipal User currentUser, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return partecipationService.getAllByTeam(currentUser, page, size, sortBy, teamId);
    }

    @PostMapping("/{teamId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public Partecipation createPartecipation(@PathVariable UUID teamId, @RequestBody @Validated NewPartecipationDTO body, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return partecipationService.createPartecipation(teamId, currentUser, body);
    }

    @GetMapping("/{partecipationId}/accept")
    public void acceptPartecipation(@PathVariable UUID partecipationId, HttpServletResponse response) throws IOException {
        partecipationService.acceptPartecipation(partecipationId, response);
    }

    @GetMapping("/{partecipationId}/reject")
    public void rejectPartecipation(@PathVariable UUID partecipationId, HttpServletResponse response) throws IOException {
        partecipationService.rejectPartecipation(partecipationId, response);
    }

    @DeleteMapping("/{partecipationId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    public void deleteById(@PathVariable UUID partecipationId, @AuthenticationPrincipal User currentUser) {
        partecipationService.deleteById(partecipationId, currentUser);
    }
}
