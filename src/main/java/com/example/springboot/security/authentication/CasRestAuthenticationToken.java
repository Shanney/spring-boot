package com.example.springboot.security.authentication;

import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public class CasRestAuthenticationToken extends CasAuthenticationToken {

    private String ssoTgt;

    /**
     * Constructor.
     *
     * @param key         to identify if this object made by a given
     *                    {@link CasAuthenticationProvider}
     * @param principal   typically the UserDetails object (cannot be <code>null</code>)
     * @param credentials the service/proxy ticket ID from CAS (cannot be
     *                    <code>null</code>)
     * @param authorities the authorities granted to the user (from the
     *                    {@link UserDetailsService}) (cannot
     *                    be <code>null</code>)
     * @param userDetails the user details (from the
     *                    {@link UserDetailsService}) (cannot
     *                    be <code>null</code>)
     * @param assertion   the assertion returned from the CAS servers. It contains the
     *                    principal and how to obtain a proxy ticket for the user.
     * @throws IllegalArgumentException if a <code>null</code> was passed
     */
    public CasRestAuthenticationToken(String key, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, UserDetails userDetails, Assertion assertion, String ssoTgt) {
        super(key, principal, credentials, authorities, userDetails, assertion);
        this.ssoTgt = ssoTgt;
    }

    public String getSsoTgt() {
        return ssoTgt;
    }
}
