/**
 * 
 */
package edu.stanford.laneweb;

import junit.framework.TestCase;

/**
 * @author ceyates
 *
 */
public class QueryTranslatorTest extends TestCase {

	private QueryTranslator translator;
	protected void setUp() throws Exception {
		super.setUp();
		this.translator = new QueryTranslator();
	}

	/**
	 * Test method for {@link edu.stanford.laneweb.QueryTranslator#processString(java.lang.String)}.
	 */
	public void testProcessString() {
		this.translator.processString("green  red");
		assertEquals("(({green} & {red})) ",this.translator.getQuery());
		this.translator.processString("green red");
		assertEquals("(({green} & {red})) ",this.translator.getQuery());
		this.translator.processString("green red -blue");
		assertEquals("(({green} & {red}))  NOT {blue}",this.translator.getQuery());
		this.translator.processString("green  red  -blue");
		assertEquals("(({green} & {red}))  NOT {blue}",this.translator.getQuery());
	}


}
