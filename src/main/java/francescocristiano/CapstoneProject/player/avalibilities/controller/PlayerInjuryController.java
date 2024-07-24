package francescocristiano.CapstoneProject.player.avalibilities.controller;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.player.avalibilities.PlayerInjuryPeriod;
import francescocristiano.CapstoneProject.player.avalibilities.payload.NewPlayerInjuryDTO;
import francescocristiano.CapstoneProject.player.avalibilities.service.PlayerInjuryPeriodService;
import francescocristiano.CapstoneProject.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/player-injury")
public class PlayerInjuryController {

    @Autowired
    private PlayerInjuryPeriodService playerInjuryPeriodService;

    @GetMapping("/{playerId}")
    public PlayerInjuryPeriod findByPlayerId(@PathVariable UUID playerId) {
        return playerInjuryPeriodService.findByPlayerId(playerId);
    }

    @PostMapping("/{playerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerInjuryPeriod save(@PathVariable UUID playerId, @RequestBody @Validated NewPlayerInjuryDTO playerInjuryDTO, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return playerInjuryPeriodService.createInjuryPeriod(playerId, playerInjuryDTO, currentUser);
    }

    @DeleteMapping("/{playerId}/{injuryId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID playerId, @PathVariable UUID injuryId, @AuthenticationPrincipal User currentUser) {
        playerInjuryPeriodService.delete(playerId, injuryId, currentUser);
    }

    @PutMapping("/{playerId}/{injuryId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.OK)
    public PlayerInjuryPeriod update(@PathVariable UUID playerId, @PathVariable UUID injuryId, @RequestBody @Validated NewPlayerInjuryDTO playerInjuryDTO, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return playerInjuryPeriodService.update(playerId, injuryId, playerInjuryDTO, currentUser);
    }
}
