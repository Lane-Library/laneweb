/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.ResourceMap;
import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

/**
 * @author ceyates
 *
 */
public class SpellCheckGenerator extends ServiceableGenerator {
	
	private static final String SPELLCHECK = "spellcheck";
	private static final String QUERY = "query";
	private static final String SUGGESTION = "suggestion";
	private static final String NAMESPACE = "http://lane.stanford.edu/spellcheck/ns";
	
	private SpellChecker spellChecker;
	private String query;

	@Override
	public void dispose() {
		this.manager.release(this.spellChecker);
		super.dispose();
	}

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		this.spellChecker = (SpellChecker) this.manager.lookup(SpellChecker.class.getName());
	}

	/* (non-Javadoc)
	 * @see org.apache.cocoon.generation.Generator#generate()
	 */
	public void generate() throws IOException, SAXException,
			ProcessingException {
		SpellCheckResult result = this.spellChecker.spellCheck(this.query);
		this.contentHandler.startDocument();
		XMLUtils.startElement(this.contentHandler, NAMESPACE, SPELLCHECK);
		XMLUtils.createElementNS(this.contentHandler, NAMESPACE, QUERY, this.query);
		XMLUtils.createElementNS(this.contentHandler, NAMESPACE, SUGGESTION, result.getSuggestion());
		XMLUtils.endElement(this.contentHandler, NAMESPACE, SPELLCHECK);
		this.contentHandler.endDocument();
	}

	@Override
	public void recycle() {
		this.query = null;
		super.recycle();
	}

	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters params) throws ProcessingException, SAXException,
			IOException {
		super.setup(resolver, objectModel, src, params);
		this.query = params.getParameter(QUERY, null);
		if (null == this.query || this.query.length() == 0) {
			throw new ProcessingException("null or empty query");
		}
	}

}
