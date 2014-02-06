/**
 * 
 */
package edu.stanford.irt.laneweb.seminars;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

/**
 * @author ryanmax
 */
public class SeminarsLinkGeneratorTest {

    private SeminarsLinkGenerator generator;

    private SAXStrategy<Map<String, String>> saxStrategy;

    private XMLConsumer xmlConsumer;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new SeminarsLinkGenerator(this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public final void testGenerate() {
        this.generator.setParameters(Collections.<String, String> emptyMap());
        this.saxStrategy.toSAX(isA(Map.class), eq(this.xmlConsumer));
        replay(this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.saxStrategy, this.xmlConsumer);
    }
}
