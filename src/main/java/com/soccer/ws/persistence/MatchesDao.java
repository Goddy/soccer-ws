package com.soccer.ws.persistence;

import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.model.Doodle;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Season;
import com.soccer.ws.model.Team;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by u0090265 on 5/3/14.
 */
public interface MatchesDao extends PagingAndSortingRepository<Match, java.util.UUID>, JpaSpecificationExecutor<Match> {

    @Query("select m from Match m where m.season = ?1 order by date asc")
    List<Match> getMatchesForSeason(Season season);

    @Query("select m from Match m where m.homeTeam = ?1 OR awayTeam = ?1 order by date desc")
    List<Match> getMatchesForTeam(Team team);

    @Query("select m from Match m where m.date > ?1 order by date asc")
    List<Match> findByDate(DateTime date);

    List<Match> findByStatusAndDateAfterOrderByDateDesc(MatchStatusEnum matchStatusEnum, DateTime date);

    Page<Match> findByDateAfter(DateTime date, Pageable pageable);

    Page<Match> findAll(Pageable pageable);

    Page<Match> findByMotmPollNotNull(Pageable pageable);

    Optional<Match> findByMotmPollId(UUID pollId);

    Optional<Match> findByMatchDoodle(Doodle doodle);

    Match findFirstByDateBeforeAndMotmPollIsNotNullOrderByDateDesc(DateTime date);
}
