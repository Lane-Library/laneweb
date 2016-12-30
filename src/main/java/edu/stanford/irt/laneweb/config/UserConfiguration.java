package edu.stanford.irt.laneweb.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import edu.stanford.irt.laneweb.user.GSSAPIAuthenticationStrategy;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
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
    public LDAPDataAccess ldapDataAccess() {
        Set<String> activeAffiliations = new HashSet<>();
        activeAffiliations.add("stanford:affiliate:sponsored");
        activeAffiliations.add("stanford:faculty*");
        activeAffiliations.add("stanford:faculty:onleave");
        activeAffiliations.add("stanford:faculty:otherteaching");
        activeAffiliations.add("stanford:faculty:slac");
        activeAffiliations.add("stanford:faculty");
        activeAffiliations.add("stanford:faculty:affiliate");
        activeAffiliations.add("stanford:faculty:emeritus");
        activeAffiliations.add("stanford:faculty:retired");
        activeAffiliations.add("stanford:staff*");
        activeAffiliations.add("stanford:staff");
        activeAffiliations.add("stanford:staff:academic");
        activeAffiliations.add("stanford:faculty:onleave");
        activeAffiliations.add("stanford:staff:emeritus");
        activeAffiliations.add("stanford:staff:otherteaching");
        activeAffiliations.add("stanford:staff:temporary");
        activeAffiliations.add("stanford:staff:casual");
        activeAffiliations.add("stanford:student*");
        activeAffiliations.add("stanford:student");
        activeAffiliations.add("stanford:student:onleave");
        activeAffiliations.add("stanford:student:postdoc");
        activeAffiliations.add("stanford:student:mla");
        activeAffiliations.add("stanford:student:ndo");
        activeAffiliations.add("sumc:staff");
        return new LDAPDataAccess(new LdapTemplate(ldapContextSource()), subjectSource(), activeAffiliations);
    }

    @Bean
    public SubjectSource subjectSource() {
        return new SubjectSource("IRT_K5");
    }
}
