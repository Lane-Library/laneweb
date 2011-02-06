package edu.stanford.irt.laneweb.security;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class WebAuthUserDetailsService implements UserDetailsService {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static final String ROLE_USER = "ROLE_USER";

    private static final GrantedAuthority ADMIN_AUTHORITY = new GrantedAuthorityImpl(ROLE_ADMIN);

    private static final GrantedAuthority USER_AUTHORITY = new GrantedAuthorityImpl(ROLE_USER);

    private Collection<GrantedAuthority> adminAuthority = Collections.emptySet();

    private Collection<GrantedAuthority> userAuthority = Collections.singleton(USER_AUTHORITY);

    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {
        return new WebAuthUserDetails(username, null, null);
    }

    public void setAdminUsers(final Set<String> adminUsers) {
        this.adminAuthority = new LinkedList<GrantedAuthority>();
        this.adminAuthority.add(ADMIN_AUTHORITY);
        this.adminAuthority.add(USER_AUTHORITY);
    }
}
