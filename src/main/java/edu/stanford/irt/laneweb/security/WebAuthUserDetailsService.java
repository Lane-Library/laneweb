package edu.stanford.irt.laneweb.security;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
    
    private List<String> adminUsers;

    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {
        Collection<GrantedAuthority> authorities;
        if (this.adminUsers.contains(username)) {
            authorities = this.adminAuthority;
        } else {
            authorities = this.userAuthority;
        }
        return new WebAuthUserDetails(username, "", authorities);
    }

    public void setAdminUsers(final List<String> adminUsers) {
        this.adminUsers = adminUsers;
        this.adminAuthority = new LinkedList<GrantedAuthority>();
        this.adminAuthority.add(ADMIN_AUTHORITY);
        this.adminAuthority.add(USER_AUTHORITY);
    }
}
