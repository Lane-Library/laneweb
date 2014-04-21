package edu.stanford.irt.laneweb.search;

import java.util.HashMap;
import java.util.List;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.bassett.BassettImage;

public class ImageBassettSearchSAXStrategy extends AbstractImageSearchSAXStrategy<HashMap<String, Object>> {

	private final int MAX_BASSETT_RESULT = 12;
	private static final String BASSETT_PAGE_PATH = "/biomed-resources/bassett/bassettView.html?bn=";
	private static final String BASSETT_ICON_SRC_URI = "http://elane.stanford.edu/public/L254573/small/";
	private static final String BASSETT_SEARCH_URL = "http://lane.stanford.edu/search.html?source=bassett&q=";

	
	@SuppressWarnings("unchecked")
	public void toSAX(HashMap<String, Object> result, XMLConsumer xmlConsumer) {
		try {
			String query = (String) result.get(ImageSearchGenerator.SEARCH_TERM);
			List<BassettImage> bassettResult = (List<BassettImage>) result.get(ImageSearchGenerator.BASSETT_RESULT);
			int bassettMaxResult = ((bassettResult.size() > MAX_BASSETT_RESULT) ? MAX_BASSETT_RESULT : bassettResult.size());
			
			if (bassettResult.size() != 0) {
				startDiv(xmlConsumer);
				createTitle(xmlConsumer, "bassett", "Bassett", String.valueOf(bassettMaxResult), String.valueOf(bassettResult.size()), BASSETT_SEARCH_URL.concat(query));
				startElementWithId(xmlConsumer, UL, "imageList");
				for (BassettImage bassettImage : bassettResult) {
					String titleAndSubtitle = bassettImage.getTitle();
					String bassettTitle = "";
					if (titleAndSubtitle != null) {
						String[] title = titleAndSubtitle.split("\\.");
						bassettTitle = title[0];
					}
					generateImages(xmlConsumer, bassettTitle, BASSETT_PAGE_PATH.concat(bassettImage.getBassettNumber()),
							BASSETT_ICON_SRC_URI.concat(bassettImage.getImage()));
					if (--bassettMaxResult == 0)
						break;
				}
				endUl(xmlConsumer);
				endDiv(xmlConsumer);
			}
		} catch (SAXException e) {
			throw new LanewebException(e);
		}
	}

}
