package com.soccer.ws.service;

import com.soccer.ws.dto.*;
import com.soccer.ws.model.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

/**
 * Created by u0090265 on 10/2/15.
 */
public interface DTOConversionHelper {
    List<MatchDTO> convertMatches(List<Match> matchList, boolean isLoggedIn);

    MatchDTO convertMatch(Match match, boolean isLoggedIn);

    List<AddressDTO> convertAddressList(List<Address> addressList);

    List<TeamDTO> convertTeams(List<Team> teamList, boolean isLoggedIn);

    TeamDTO convertTeam(Team team, boolean isLoggedIn);

    MatchPollDTO convertMatchPoll(Match match, boolean isLoggedIn);

    PageDTO<MatchPollDTO> convertMatchPolls(Page<Match> matches, boolean isLoggedIn);

    List<AccountDTO> convertIdentityOptions(Set<IdentityOption> identityOptions, boolean isLoggedIn);

    List<VotesDTO> convertIdentityRankings(RankingList<UUID> rankingList, boolean isLoggedIn);

    List<SeasonDTO> convertSeasons(List<Season> seasons);

    SeasonDTO convertSeason(Season season);

    List<GoalDTO> convertGoals(SortedSet<Goal> goals, boolean isLoggedIn);

    AccountDTO convertAccount(Account account, boolean isLoggedIn);

    List<AccountDTO> convertAccounts(List<Account> account, boolean isLoggedIn);

    Account convertAccount(AccountDTO account);

    PageDTO<MatchDoodleDTO> convertMatchDoodles(Page<Match> match, Account account, boolean isAdmin);

    MatchDoodleDTO convertMatchDoodle(Match match, Account account, boolean isAdmin);

    DoodleDTO convertDoodle(Match match, Account account, boolean isAdmin);

    PresenceDTO convertPresence(Presence presence, boolean isLoggedIn);

    PageDTO<NewsDTO> convertNewsPage(Account account, Page<News> page, boolean isAdmin);

    NewsDTO convertNews(Account account, News news, boolean isAdmin);

    CommentDTO convertComment(Account account, Comment comment, boolean isAdmin, boolean isLoggedIn);

    ProfileDTO convertProfile(AccountProfile profile, boolean isLoggedIn);

    ImageDTO convertImage(Image image);
}
