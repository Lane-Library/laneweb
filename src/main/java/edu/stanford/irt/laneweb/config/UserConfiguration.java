package edu.stanford.irt.laneweb.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import edu.stanford.irt.laneweb.user.GSSAPIAuthenticationStrategy;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.LoginContextFactory;
import edu.stanford.irt.laneweb.user.SubjectSource;

@Configuration
public class UserConfiguration {

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://ldap.stanford.edu/");
        contextSource.setBase("cn=people,dc=stanford,dc=edu");
        contextSource.setAuthenticationStrategy(new GSSAPIAuthenticationStrategy());
        contextSource
                .setBaseEnvironmentProperties(Collections.singletonMap("com.sun.jndi.ldap.connect.timeout", "5000"));
        return contextSource;
    }

    @Bean
    public LDAPDataAccess ldapDataAccess(final LdapTemplate ldapTemplate, final SubjectSource subjectSource) {
        List<String> privilegeGroup = new ArrayList<String>(3);
        privilegeGroup.add("stanford:library-eresources-eligible");
        privilegeGroup.add("lane:proxy-access");
        privilegeGroup.add("sulair:proxy-access");
        return new LDAPDataAccess(ldapTemplate, subjectSource, privilegeGroup);
    }

    @Bean
    public LdapTemplate ldapTemplate(final LdapContextSource ldapContextSource) {
        return new LdapTemplate(ldapContextSource);
    }

    @Bean
    public LoginContextFactory loginContextFactory() {
        return new LoginContextFactory("IRT_K5");
    }

    @Bean
    public SubjectSource subjectSource(final LoginContextFactory loginContextFactory) {
        return new SubjectSource(loginContextFactory);
    }
}
