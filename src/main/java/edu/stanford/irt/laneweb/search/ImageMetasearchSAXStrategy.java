package edu.stanford.irt.laneweb.search;

import java.util.Collection;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ImageMetasearchSAXStrategy extends AbstractImageSearchSAXStrategy<Result> {

	@Override
	public void toSAX(Result metaSearchResult, XMLConsumer xmlConsumer) {
		try {
			Collection<Result> engines = metaSearchResult.getChildren();
			String hits = null;
			for (Result engineResult : engines) {
				startDiv(xmlConsumer);
				Collection<Result> resources = engineResult.getChildren();
				hits = engineResult.getHits();
				String url = null;
				for (Result resource : resources) {
					if (resource.getId().indexOf("_content") == -1) {
						url = resource.getURL();
					}
					if (resource.getHits() != null && !"0".equals(resource.getHits()) && resource.getId().endsWith("_content")) {
						createTitle(xmlConsumer, resource.getId(), engineResult.getDescription(), resource.getHits(), hits, url);
					}
					startElementWithId(xmlConsumer, UL, "imageList");
					Collection<Result> contents = resource.getChildren();
					for (Result content : contents) {
						if (content.getId().contains("_content_")) {
							ContentResult contentResult = (ContentResult) content;
							generateImages(xmlConsumer, contentResult.getTitle(), contentResult.getURL(), contentResult.getDescription());
						}
					}
					endUl(xmlConsumer);
				}
				endDiv(xmlConsumer);

			}
		} catch (SAXException e) {
			throw new LanewebException(e);
		}
	}
}
