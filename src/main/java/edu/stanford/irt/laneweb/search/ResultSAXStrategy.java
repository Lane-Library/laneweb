package edu.stanford.irt.laneweb.search;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.SAXResult;
import edu.stanford.irt.search.impl.Result;

public class ResultSAXStrategy implements SAXStrategy<Result> {

    @Override
    public void toSAX(final Result result, final XMLConsumer xmlConsumer) {
        try {
            new SAXResult(result).toSAX(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
