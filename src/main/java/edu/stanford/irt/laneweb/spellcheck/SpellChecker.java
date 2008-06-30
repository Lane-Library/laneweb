/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.avalon.framework.activity.Initializable;

import edu.stanford.irt.spell.GoogleAPISpellChecker;

/**
 * @author ceyates
 */
public class SpellChecker extends GoogleAPISpellChecker implements
        Initializable {

    public void initialize() throws NamingException {
        Context context = new InitialContext();
        String googleKey = (String) context.lookup("java:comp/env/google-key");
        super.setGoogleKey(googleKey);
    }

}
