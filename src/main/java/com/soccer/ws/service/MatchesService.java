package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * Created by u0090265 on 5/3/14.
 */
public interface MatchesService {
    Map<Integer, List<Match>> getMatchesForLastSeasons();

    List<Match> getMatchesListForSeason(Season season);

    Page<Match> getUpcomingMatchesPages(int page, int pageSize, Optional<Sort> sort);

    List<MatchDTO> getMatchesForSeason(long seasonId, Account account);

    List<Match> getMatchesForSeason(long seasonId);

    List<Match> getMatchesForSeason(String description);

    Match getLatestMatch();

    Match getLatestMatchWithPoll();

    Page<Match> getMatchesWithPolls(int page, int pageSize, Optional<Sort> sort, Optional<String> searchTerm);

    Match getMatch(long id);

    //Match createMatch(CreateMatchForm form) throws ParseException;

    //Match updateMatch(ChangeResultForm form);

    void deleteMatch(long id) throws ObjectNotFoundException;
}