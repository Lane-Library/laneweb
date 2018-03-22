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
    public LDAPDataAccess ldapDataAccess(final LdapTemplate ldapTemplate, final SubjectSource subjectSource) {
        Set<String> activeAffiliations = new HashSet<>(24);
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
        return new LDAPDataAccess(ldapTemplate, subjectSource, activeAffiliations);
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://ldap.stanford.edu/");
        ldapContextSource.setBase("cn=people,dc=stanford,dc=edu");
        ldapContextSource.setAuthenticationStrategy(new GSSAPIAuthenticationStrategy());
        ldapContextSource
                .setBaseEnvironmentProperties(Collections.singletonMap("com.sun.jndi.ldap.connect.timeout", "5000"));
        return new LdapTemplate(ldapContextSource);
    }

    @Bean
    public SubjectSource subjectSource() {
        return new SubjectSource("IRT_K5");
    }
}
