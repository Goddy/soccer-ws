package com.soccer.ws.controllers;

import com.google.common.base.Optional;
import com.soccer.ws.dto.MatchDoodleDTO;
import com.soccer.ws.dto.PageDTO;
import com.soccer.ws.dto.PresenceDTO;
import com.soccer.ws.model.Match;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.DoodleService;
import com.soccer.ws.service.MatchesService;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by u0090265 on 09/09/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Doodle REST api", description = "Doodle REST operations")
public class DoodleRestController extends AbstractRestController {
    private final MatchesService matchesService;
    private final DTOConversionHelper DTOConversionHelper;
    private final DoodleService doodleService;

    public DoodleRestController(SecurityUtils securityUtils, MessageSource messageSource, MatchesService matchesService, DTOConversionHelper dtoConversionHelper, DoodleService doodleService) {
        super(securityUtils, messageSource);
        this.matchesService = matchesService;
        DTOConversionHelper = dtoConversionHelper;
        this.doodleService = doodleService;
    }


    @RequestMapping(value = "/matchDoodle", method = RequestMethod.GET)
    @ApiOperation(value = "Get matchdoodles", nickname = "matchdoodles")
    public ResponseEntity<PageDTO<MatchDoodleDTO>> getMatchDoodles(@RequestParam int page, @RequestParam(required =
            false) int size) {
        Page<Match> matches = matchesService.getUpcomingMatchesPages(page, size, Optional.absent());
        return new ResponseEntity<>(DTOConversionHelper.convertMatchDoodles(matches, getAccountFromSecurity(),
                isAdmin()), HttpStatus.OK);
    }

    @RequestMapping(value = "/matchDoodle/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get matchdoodles", nickname = "matchdoodles")
    public ResponseEntity<MatchDoodleDTO> getMatchDoodle(@PathVariable Long id) {
        Match m = matchesService.getMatch(id);
        return new ResponseEntity<>(DTOConversionHelper.convertMatchDoodle(m, getAccountFromSecurity(),
                isAdmin()), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/doodle/match/{id}/presence/{accountId}", method = RequestMethod.PUT)
    @ApiOperation(value = "Get matchdoodles", nickname = "matchdoodles")
    public ResponseEntity<PresenceDTO> changePresence(@PathVariable Long id, @PathVariable Long accountId) {
        doodleService.changePresence(getAccountFromSecurity(), accountId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
