package com.soccer.ws.local;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.soccer.ws.migration.model.*;
import com.soccer.ws.migration.persistence.*;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.BaseClass;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Poll;
import com.soccer.ws.persistence.*;
import org.apache.commons.lang.RandomStringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerConverter;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.dozer.loader.api.FieldsMappingOptions.copyByReference;

@Component
@Profile("realdb")
public class PrimaryKeyUpdater {
    private static final Logger log = LoggerFactory.getLogger(PrimaryKeyUpdater.class);
    private final NewAccountDao new_New_accountDao;
    private final NewAddressDao new_New_addressDao;
    private final NewCommentDao new_New_commentDao;
    private final NewDoodleDao new_New_doodleDao;
    private final NewMatchesDao new_New_matchesDao;
    private final NewNewsDao new_New_newsDao;
    private final NewPollDao new_New_pollDao;
    private final NewSeasonDao new_New_seasonDao;
    private final NewTeamDao new_New_teamDao;
    private final AccountDao accountDao;
    private final AddressDao addressDao;
    private final CommentDao commentDao;
    private final DoodleDao doodleDao;
    private final MatchesDao matchesDao;
    private final NewsDao newsDao;
    private final PollDao pollDao;
    private final SeasonDao seasonDao;
    private final TeamDao teamDao;
    private final NewJdbcNewUserDetailsDao newJdbcNewUserDetailsDao;
    private final JdbcUserDetailsDao jdbcUserDetailsDao;

    PrimaryKeyUpdater(NewAccountDao new_New_accountDao, NewAddressDao new_New_addressDao, NewCommentDao new_New_commentDao, NewDoodleDao new_New_doodleDao, NewMatchesDao new_New_matchesDao, NewNewsDao new_New_newsDao, NewPollDao new_New_pollDao, NewSeasonDao new_New_seasonDao, NewTeamDao new_New_teamDao, AccountDao accountDao, AddressDao addressDao, CommentDao commentDao, DoodleDao doodleDao, MatchesDao matchesDao, NewsDao newsDao, PollDao pollDao, SeasonDao seasonDao, TeamDao teamDao, NewJdbcNewUserDetailsDao newJdbcNewUserDetailsDao, JdbcUserDetailsDao jdbcUserDetailsDao) {
        this.new_New_accountDao = new_New_accountDao;
        this.new_New_addressDao = new_New_addressDao;
        this.new_New_commentDao = new_New_commentDao;
        this.new_New_doodleDao = new_New_doodleDao;
        this.new_New_matchesDao = new_New_matchesDao;
        this.new_New_newsDao = new_New_newsDao;
        this.new_New_pollDao = new_New_pollDao;
        this.new_New_seasonDao = new_New_seasonDao;
        this.new_New_teamDao = new_New_teamDao;
        this.accountDao = accountDao;
        this.addressDao = addressDao;
        this.commentDao = commentDao;
        this.doodleDao = doodleDao;
        this.matchesDao = matchesDao;
        this.newsDao = newsDao;
        this.pollDao = pollDao;
        this.seasonDao = seasonDao;
        this.teamDao = teamDao;
        this.newJdbcNewUserDetailsDao = newJdbcNewUserDetailsDao;
        this.jdbcUserDetailsDao = jdbcUserDetailsDao;

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(BaseClass.class, NewBaseClass.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("created", "created",
                                copyByReference()
                        )
                        .fields("modified", "modified",
                                copyByReference()
                        );
                mapping(Account.class, NewAccount.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("passwordLastSet", "passwordLastSet",
                                copyByReference()
                        );
                mapping(Match.class, NewMatch.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("date", "date",
                                copyByReference()
                        );
                mapping(Poll.class, NewPoll.class,
                        TypeMappingOptions.oneWay()
                )
                        .fields("startDate", "startDate",
                                copyByReference()
                        )
                        .fields("endDate", "endDate",
                                copyByReference()
                        );
            }
        };

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(builder);

        mapper.setCustomConverters(Lists.newArrayList(new DateTimeCustomConverter()));

        this.accountDao.findAll().forEach(e -> {
            try {
                NewAccount o = new NewAccount();
                mapper.map(e, o);
                log.info("Extracted account " + o);
                String password = jdbcUserDetailsDao.findPasswordByUsername(o.getUsername());
                o.setPassword(password == null ? RandomStringUtils.randomAlphanumeric(40) : password);
                new_New_accountDao.save(o);
            } catch (Exception ex) {
                log.info("Could not copy account " + e);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        Set<NewAccount> accounts = Sets.newHashSet(new_New_accountDao.findAll().iterator());
        Set<Account> oldAccounts = Sets.newHashSet(accountDao.findAll().iterator());

        this.seasonDao.findAll().forEach(e -> {
            try {
                NewSeason o = new NewSeason();
                mapper.map(e, o);
                log.info("Extracted season " + o);
                new_New_seasonDao.save(o);
            } catch (Exception ex) {
                log.info("Could not copy season " + e);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        this.addressDao.findAll().forEach(e -> {
            try {
                NewAddress o = new NewAddress();
                mapper.map(e, o);
                log.info("Extracted address " + o);
                new_New_addressDao.save(o);
            } catch (Exception ex) {
                log.info("Could not copy address " + e);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });


        Set<NewAddress> addresses = Sets.newHashSet(new_New_addressDao.findAll().iterator());

        this.teamDao.findAll().forEach(e -> {
            try {
                NewTeam o = new NewTeam();
                mapper.map(e, o);
                log.info("Extracted team " + o);
                addresses.stream().filter(a -> a.getAddress().toString().equals(o.getAddress().toString())).findFirst().ifPresent(a -> o.setAddress(a));
                new_New_teamDao.save(o);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.info("Could not copy team " + e);
            }
        });

        this.newsDao.findAll().forEach(e -> {
            try {
                NewNews o = new NewNews();
                mapper.map(e, o);
                log.info("Extracted news " + o);
                accounts.stream().filter(a -> a.getUsername().equals(o.getAccount().getUsername())).findFirst().ifPresent(o::setAccount);
                o.getComments().parallelStream().forEach(c -> {
                    accounts.stream().filter(a -> a.getUsername().equals(c.getAccount().getUsername())).findFirst().ifPresent(c::setAccount);
                });

                new_New_newsDao.save(o);
            } catch (Exception ex) {
                log.info("Could not copy new " + e);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });

        Set<NewTeam> teams = Sets.newHashSet(new_New_teamDao.findAll().iterator());
        Set<NewSeason> seasons = Sets.newHashSet(new_New_seasonDao.findAll().iterator());

        this.matchesDao.findAll().forEach(e -> {
            try {
                NewMatch o = new NewMatch();
                mapper.map(e, o);
                log.info("Extracted match " + o);
                teams.stream().filter(a -> a.getName().equals(o.getHomeTeam().getName())).findFirst().ifPresent(o::setHomeTeam);
                teams.stream().filter(a -> a.getName().equals(o.getAwayTeam().getName())).findFirst().ifPresent(o::setAwayTeam);
                seasons.stream().filter(a -> a.getDescription().equals(o.getSeason().getDescription())).findFirst().ifPresent(o::setSeason);

                NewDoodle doodle = new NewDoodle();
                mapper.map(e.getMatchDoodle(), doodle);
                doodle.getPresences().parallelStream().forEach(c -> {
                    accounts.stream().filter(a -> a.getUsername().equals(c.getAccount().getUsername())).findFirst().ifPresent(c::setAccount);
                });
                doodle.setStatus(o.getMatchDoodle().getStatus());
                o.setMatchDoodle(doodle);

                Set<NewIdentityNewOption> identityOptions = Sets.newHashSet();
                Set<NewMultipleChoicePlayerNewVote> multipleChoicePlayerNewVotes = Sets.newHashSet();

                if (e.getMotmPoll() != null) {
                    identityOptions = e.getMotmPoll().getOptions().parallelStream().map(ob -> {
                        NewAccount a = oldAccounts.stream()
                                .filter(acc -> acc.getId().equals(ob.getOption()))
                                .flatMap(fa -> accounts.stream().filter(na -> na.getUsername().equals(fa.getUsername())))
                                .findFirst().orElse(null);

                        NewIdentityNewOption option = new NewIdentityNewOption();
                        mapper.map(ob, option);
                        assert a != null;
                        option.setOption(a.getNewId());
                        return option;
                    }).collect(Collectors.toSet());

                    multipleChoicePlayerNewVotes = e.getMotmPoll().getVotes().stream().map(ob -> {
                        NewMultipleChoicePlayerNewVote v = new NewMultipleChoicePlayerNewVote();
                        v.setPoll(o.getMotmPoll());
                        mapper.map(ob, v);
                        accounts.stream().filter(a -> a.getUsername().equals(ob.getVoter().getUsername())).findFirst().ifPresent(v::setVoter);
                        oldAccounts.stream()
                                .filter(a -> ob.getAnswer().equals(a.getId()))
                                .findFirst()
                                .map(a -> accounts.parallelStream().filter(na -> na.getUsername().equals(a.getUsername())).findFirst())
                                .ifPresent(a -> v.setAnswer(a.get().getNewId()));
                        return v;
                    }).collect(Collectors.toSet());
                }


                Set<NewGoal> goals = e.getGoals().stream().map(g -> {
                    NewGoal goal = new NewGoal();
                    mapper.map(g, goal);
                    g.setMatch(e);

                    if (g.getScorer() != null)
                        accounts.stream().filter(a -> a.getUsername().equals(g.getScorer().getUsername())).findFirst().ifPresent(goal::setScorer);
                    if (g.getAssist() != null)
                        accounts.stream().filter(a -> a.getUsername().equals(g.getAssist().getUsername())).findFirst().ifPresent(goal::setAssist);
                    return goal;
                }).collect(Collectors.toSet());

                o.setGoals(new TreeSet<>());


                if (o.getMotmPoll() != null) {
                    o.getMotmPoll().setOptions(identityOptions);
                    o.getMotmPoll().setVotes(multipleChoicePlayerNewVotes);
                }

                NewMatch m = new_New_matchesDao.save(o);

                m.setGoals(new TreeSet<>(goals.stream().peek(g -> g.setMatch(m)).collect(Collectors.toSet())));

                new_New_matchesDao.save(m);

            } catch (Exception ex) {
                log.info("Could not copy match " + e);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        });


    }

    public static class DateTimeCustomConverter extends DozerConverter<DateTime, DateTime> {

        public DateTimeCustomConverter() {
            super(DateTime.class, DateTime.class);
        }

        @Override
        public DateTime convertTo(final DateTime source, final DateTime destination) {

            if (source == null) {
                return null;
            }

            return new DateTime(source);
        }

        @Override
        public DateTime convertFrom(final DateTime source, final DateTime destination) {

            if (source == null) {
                return null;
            }

            return new DateTime(source);
        }

    }
}