package com.soccer.ws.controllers;

import com.soccer.ws.model.Account;
import com.soccer.ws.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by u0090265 on 14/06/16.
 */
public abstract class AbstractSecurityController {
    private final SecurityUtils securityUtils;

    @Autowired
    public AbstractSecurityController(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public Account getAccountFromSecurity() {
        return securityUtils.getAccountFromSecurity();
    }

    public boolean isLoggedIn() {
        return securityUtils.isloggedIn();
    }

    public boolean isAdmin() {
        return securityUtils.isAdmin(getAccountFromSecurity());
    }
}
