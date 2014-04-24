package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.bassett.BassettImage;

public class ImageBassettSearchSAXStrategyTest{

	private TestXMLConsumer xmlConsumer;
	
	private ImageBassettSearchSAXStrategy strategy;
	

	@Before
    public void setUp() {
     this.xmlConsumer = new TestXMLConsumer();
     this.strategy = new ImageBassettSearchSAXStrategy();
	}
	
	
	@Test
	public void testToSAX() throws SAXException, IOException {
		xmlConsumer.startDocument();
		HashMap<String, Object> res = new HashMap<String, Object>();
	    BassettImage bassettImage = new BassettImage("description", "title");
	    bassettImage.setBassettNumber("bassettNumber");
	    bassettImage.setImage("bassettSrc");
	    List<BassettImage> bassettImages = new ArrayList<>();
	    bassettImages.add(bassettImage);
		res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
		res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
		this.strategy.toSAX(res, xmlConsumer);
		xmlConsumer.endDocument();
		assertEquals(this.xmlConsumer.getExpectedResult(this, "ImageBassettSearchSAXStrategyTest-testToSAX.xml"),this.xmlConsumer.getStringValue());
	}  
	
	@Test
	public void testToSAXMaxImage() throws SAXException, IOException {
		xmlConsumer.startDocument();
		HashMap<String, Object> res = new HashMap<String, Object>();
	     List<BassettImage> bassettImages = new ArrayList<>();
	    for (int i = 1; i < 20; i++) {
	    	BassettImage bassettImage = new BassettImage("description"+i, "title"+1);
		    bassettImage.setBassettNumber("bassettNumber_"+i);
		    bassettImage.setImage("bassettSrc"+i);
	    	 bassettImages.add(bassettImage);
		}
		res.put(ImageSearchGenerator.BASSETT_RESULT, bassettImages);
		res.put(ImageSearchGenerator.SEARCH_TERM, "skin");
		this.strategy.toSAX(res, xmlConsumer);
		xmlConsumer.endDocument();
		String content = this.xmlConsumer.getStringValue();
		assertTrue(content.contains("bassettNumber_12"));
		assertTrue( !content.contains("bassettNumber_13"));
	}  
	
}



