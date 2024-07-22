package francescocristiano.CapstoneProject.player.statistics.controller;


import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.player.statistics.PlayerStatistics;
import francescocristiano.CapstoneProject.player.statistics.payload.NewPlayerStatsDTO;
import francescocristiano.CapstoneProject.player.statistics.service.PlayerStatisticsService;
import francescocristiano.CapstoneProject.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/player-statistics")
public class PlayerStatisticsController {

    @Autowired
    private PlayerStatisticsService playerStatisticsService;

    @GetMapping("/{playerId}")
    public Page<PlayerStatistics> getAllByPlayerId(@RequestParam UUID playerId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                   @AuthenticationPrincipal User currentUser) {
        return playerStatisticsService.getAllByPlayerId(playerId, page, size, sortBy, currentUser);
    }

    @PostMapping("/{playerId}/event/{eventId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerStatistics create(@PathVariable UUID playerId, @PathVariable UUID eventId, @RequestBody @Validated NewPlayerStatsDTO playerStatistics, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return playerStatisticsService.create(playerId, eventId, playerStatistics, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        playerStatisticsService.delete(id, currentUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    public PlayerStatistics update(@PathVariable UUID id, @RequestBody @Validated NewPlayerStatsDTO playerStatistics, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return playerStatisticsService.update(id, playerStatistics, currentUser);
    }


}
