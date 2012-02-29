package edu.stanford.irt.laneweb.ldap;

import java.util.List;

public class LDAPData {

    private String name;

    private String univId;

    private List<Affiliation> affiliations;
    
    public String getName() {
        return this.name;
    }

    public String getUnivId() {
        return this.univId;
    }

    public List<Affiliation> getAffiliations(){
    	return this.affiliations;
    }
    
    public void setName(final String displayName) {
        this.name = displayName;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    public void setAffiliations(final List<Affiliation> affiliations) {
        this.affiliations = affiliations;
    }

    public boolean isActive(){
    	boolean isActive = false;
    	if(null != affiliations){
			for (Affiliation affiliation : affiliations) {
				if (affiliation.isActive()) {
					isActive = true;
					break;
				}
			}
		}
    	return isActive;
    }

    
    @Override
    public String toString() {
        return new StringBuilder("univid=").append(this.univId).append(",displayname=").append(this.name).append(",isActive=").append(isActive()).toString();
    }

}
