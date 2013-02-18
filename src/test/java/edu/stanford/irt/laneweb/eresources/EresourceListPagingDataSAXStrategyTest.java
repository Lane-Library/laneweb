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
		expect(this.pagingData.getBaseQuery()).andReturn("a=a");
		expect(this.pagingData.getPagingLabels()).andReturn(Arrays.asList(new PagingLabel[]{this.pagingLabel, this.pagingLabel, this.pagingLabel, this.pagingLabel}));
		expect(this.pagingLabel.getStart()).andReturn("Aa");
		expect(this.pagingLabel.getEnd()).andReturn("Ad");
		expect(this.pagingLabel.getResults()).andReturn(260);
		expect(this.pagingLabel.getStart()).andReturn("Ad");
		expect(this.pagingLabel.getEnd()).andReturn("Am");
		expect(this.pagingLabel.getResults()).andReturn(260);
		expect(this.pagingLabel.getStart()).andReturn("Am");
		expect(this.pagingLabel.getEnd()).andReturn("Ap");
		expect(this.pagingLabel.getResults()).andReturn(260);
		expect(this.pagingLabel.getStart()).andReturn("Ap");
		expect(this.pagingLabel.getEnd()).andReturn("Ay");
		expect(this.pagingLabel.getResults()).andReturn(259);
		replay(this.pagingLabel, this.pagingData, this.list);
		this.xmlConsumer.startDocument();
		this.strategy.toSAX(this.pagingData, this.xmlConsumer);
		this.xmlConsumer.endDocument();
		System.out.println(this.xmlConsumer.getStringValue());
		assertEquals(this.xmlConsumer.getExpectedResult(this, "EresourceListPagingDataSAXStrategyTest-testToSAX.xml"), this.xmlConsumer.getStringValue());
		verify(this.pagingLabel, this.pagingData, this.list);
	}

}
