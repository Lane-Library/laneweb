package edu.stanford.laneweb;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.generation.HTMLGenerator;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;

public class ExpiringHTMLGenerator extends HTMLGenerator {
	
	private static final long DEFAULT_EXPIRATION = 60 * 60 * 1000;
	
	private long expiration;

	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		this.expiration = config.getChild("expiration").getValueAsLong(DEFAULT_EXPIRATION);
	}

	public SourceValidity getValidity() {
		return new ExpiresValidity(this.expiration);
	}

}
