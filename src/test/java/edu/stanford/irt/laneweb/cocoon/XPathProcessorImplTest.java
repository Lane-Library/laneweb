package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;

public class XPathProcessorImplTest {

	private Node node;

	private XPathProcessorImpl processor;

	@Before
	public void setUp() throws Exception {
		this.processor = new XPathProcessorImpl();
		this.node = createMock(Node.class);
	}

	@Test
	public void testEvaluateAsBooleanNodeString() {
		try {
			this.processor.evaluateAsBoolean(null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testEvaluateAsBooleanNodeStringPrefixResolver() {
		try {
			this.processor.evaluateAsBoolean(null, null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testEvaluateAsNumberNodeString() {
		try {
			this.processor.evaluateAsNumber(null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testEvaluateAsNumberNodeStringPrefixResolver() {
		try {
			this.processor.evaluateAsNumber(null, null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testEvaluateAsStringNodeString() {
		try {
			this.processor.evaluateAsString(null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testEvaluateAsStringNodeStringPrefixResolver() {
		try {
			this.processor.evaluateAsString(null, null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testSelectNodeListNodeString() {
		try {
			this.processor.selectNodeList(null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testSelectNodeListNodeStringPrefixResolver() {
		expect(this.node.getNodeType()).andReturn(Node.TEXT_NODE).times(3);
		expect(this.node.getParentNode()).andReturn(null);
		expect(this.node.getNamespaceURI()).andReturn(null);
		expect(this.node.getLocalName()).andReturn(null).times(2);
		replay(this.node);
		this.processor.selectNodeList(this.node, "/", null);
		verify(this.node);
	}

	@Test
	public void testSelectSingleNodeNodeString() {
		try {
			this.processor.selectSingleNode(null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	@Test
	public void testSelectSingleNodeNodeStringPrefixResolver() {
		try {
			this.processor.selectSingleNode(null, null, null);
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

}
