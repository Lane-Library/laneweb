package edu.stanford.irt.laneweb.search;


import java.util.HashMap;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.search.Result;

public class ImageSearchSAXStrategy extends AbstractXHTMLSAXStrategy<HashMap<String, Object>> implements
		SAXStrategy<HashMap<String, Object>> {

	ImageBassettSearchSAXStrategy bassettSAXStrategy;
	ImageMetasearchSAXStrategy metaSearchSAXStrategy;

	
	public ImageSearchSAXStrategy(ImageBassettSearchSAXStrategy bassettSAXStrategy, ImageMetasearchSAXStrategy metaSearchSAXStrategy){
		this.bassettSAXStrategy = bassettSAXStrategy;
		this.metaSearchSAXStrategy = metaSearchSAXStrategy;
	}
	
	@Override
	public void toSAX(HashMap<String, Object> result, XMLConsumer xmlConsumer) {

		try {
			xmlConsumer.startDocument();
			startDiv(xmlConsumer);
			Result metaSearchResult = (Result) result.get(ImageSearchGenerator.METASEARCH_RESULT);
			metaSearchSAXStrategy.toSAX(metaSearchResult, xmlConsumer);
			bassettSAXStrategy.toSAX(result, xmlConsumer);
			endDiv(xmlConsumer);
			xmlConsumer.endDocument();
		} catch (SAXException e) {
			throw new LanewebException(e);
		}

	}

}
