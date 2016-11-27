package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Team;
import com.soccer.ws.persistence.AddressDao;
import com.soccer.ws.persistence.MatchesDao;
import com.soccer.ws.persistence.TeamDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by u0090265 on 5/10/14.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    private static final Logger log = LoggerFactory.getLogger(TeamService.class);
    @Autowired
    ConcurrentDataService concurrentDataService;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private MatchesDao matchesDao;

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Team> getAll() {
        return Lists.newArrayList(teamDao.findAll());
    }

    @Override
    public boolean teamExists(String name) {
        return teamDao.getTeamByName(name) != null;
    }

    @Override
    public Team getTeam(long id) {
        return teamDao.findOne(id);
    }

    /**
     * @Transactional(readOnly = false)
     * @Override public Team createTeam(CreateAndUpdateTeamForm form) {
     * Team team = new Team();
     * updateTeamFromForm(form, team);
     * teamDao.save(team);
     * return team;
     * }
     * @Transactional(readOnly = false)
     * @Override public Team updateTeam(CreateAndUpdateTeamForm form) {
     * Team team = teamDao.findOne(form.getId());
     * updateTeamFromForm(form, team);
     * teamDao.save(team);
     * return team;
     * }
     * <p>
     * private void updateTeamFromForm(CreateAndUpdateTeamForm form, Team team) {
     * //If an existing address is chose, get the address, otherwise create a new one.
     * if (form.isUseExistingAddress()) {
     * Address address = addressDao.findOne(form.getAddressId());
     * if (address == null) throw new ObjectNotFoundException(String.format("Address with id %s not found", form
     * .getAddressId()));
     * team.setAddress(address);
     * } else {
     * team.setAddress(getAddress(form));
     * }
     * team.setName(form.getTeamName());
     * }
     * <p>
     * private Address getAddress(CreateAndUpdateTeamForm form) {
     * Address address = new Address();
     * address.setAddress(form.getAddress());
     * address.setCity(form.getCity());
     * address.setPostalCode(Integer.parseInt(form.getPostalCode()));
     * address.setGoogleLink(form.isUseLink() ? form.getGoogleLink() : null);
     * return address;
     * }
     **/

    @Override
    public List<TeamDTO> getTeams(final Account account) {
        return concurrentDataService.getTeams(account);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean deleteTeam(long id, Account a) {
        Team team = teamDao.findOne(id);
        if (team == null) return true;
        if (!matchesDao.getMatchesForTeam(team).isEmpty()) {
            return false;
        } else {
            teamDao.delete(team);
            return true;
        }
    }
}