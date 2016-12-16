package com.soccer.ws.persistence;

import com.google.common.collect.Sets;
import com.soccer.ws.model.Account;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:13 PM
 * Remarks: none
 */
public class UserDetailsAdapter extends SocialUser {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UserDetailsAdapter.class);
    private Account account;

    public UserDetailsAdapter(Account account, String password) {
        super(account.getUsername(), getPassword(password), Sets.newHashSet(new SimpleGrantedAuthority("ROLE_" + account
                .getRole().name())));
        this.account = account;
    }

    private static String getPassword(String pw) {
        return pw == null ? "SocialUser" : pw;
    }

    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return account.getId();
    }

    public String getFirstName() {
        return account.getFirstName();
    }

    public String getLastName() {
        return account.getLastName();
    }

    public String getFullName() {
        return account.getFullName();
    }

    public DateTime getPasswordLastSet() {
        return account.getPasswordLastSet();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));
        return authorities;
    }
}