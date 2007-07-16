/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;

import edu.stanford.irt.spell.GoogleAPISpellChecker;

/**
 * @author ceyates
 *
 */
public class SpellChecker extends GoogleAPISpellChecker implements Parameterizable{

	public void parameterize(Parameters params) throws ParameterException {
		super.setGoogleKey(params.getParameter("google-key"));
	}

}
