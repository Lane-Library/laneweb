package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;

public class EresourceListPagingDataSAXStrategyTest {

	private TestXMLConsumer xmlConsumer;
	private EresourceListPagingDataSAXStrategy strategy;
	private EresourceListPagingData pagingData;
	private PagingEresourceList list;
	private PagingLabel pagingLabel;

	@Before
	public void setUp() throws Exception {
		this.xmlConsumer = new TestXMLConsumer();
		this.strategy = new EresourceListPagingDataSAXStrategy();
		this.pagingData = createMock(EresourceListPagingData.class);
		this.list = createMock(PagingEresourceList.class);
		this.pagingLabel = createMock(PagingLabel.class);
	}

	@Test
	public void testToSAX() throws SAXException, IOException {
		expect(this.pagingData.getSize()).andReturn(1039);
		expect(this.pagingData.getLength()).andReturn(260);
		expect(this.pagingData.getStart()).andReturn(0);
		expect(this.pagingData.getBaseQuery()).andReturn("a=a").times(2);
		expect(this.pagingData.getPagingLabels()).andReturn(Arrays.asList(new PagingLabel[]{this.pagingLabel, this.pagingLabel, this.pagingLabel, this.pagingLabel}));
		expect(this.pagingLabel.getStart()).andReturn("A.M.A. American journal of diseases of children");
		expect(this.pagingLabel.getEnd()).andReturn("Advances in immunology");
		expect(this.pagingLabel.getResults()).andReturn(260);
		expect(this.pagingLabel.getStart()).andReturn("Advances in insect physiology");
		expect(this.pagingLabel.getEnd()).andReturn("American journal of tropical medicine");
		expect(this.pagingLabel.getResults()).andReturn(260);
		expect(this.pagingLabel.getStart()).andReturn("American journal of tropical medicine and hygiene");
		expect(this.pagingLabel.getEnd()).andReturn("Applied optics");
		expect(this.pagingLabel.getResults()).andReturn(260);
		expect(this.pagingLabel.getStart()).andReturn("Applied psychological measurement");
		expect(this.pagingLabel.getEnd()).andReturn("Ayu");
		expect(this.pagingLabel.getResults()).andReturn(259);
		replay(this.pagingLabel, this.pagingData, this.list);
		this.xmlConsumer.startDocument();
		this.strategy.toSAX(this.pagingData, this.xmlConsumer);
		this.xmlConsumer.endDocument();
		assertEquals(this.xmlConsumer.getExpectedResult(this, "EresourceListPagingDataSAXStrategyTest-testToSAX.xml"), this.xmlConsumer.getStringValue());
		verify(this.pagingLabel, this.pagingData, this.list);
	}

}
