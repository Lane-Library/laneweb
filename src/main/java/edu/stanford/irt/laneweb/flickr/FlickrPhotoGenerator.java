package edu.stanford.irt.laneweb.flickr;

import java.util.List;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class FlickrPhotoGenerator extends AbstractGenerator {

    private static final int RANDOM_PHOTO_COUNT = 12;

    private FlickrPhotoListService service;

    private SAXStrategy<List<FlickrPhoto>> strategy;

    public FlickrPhotoGenerator(final FlickrPhotoListService service, final SAXStrategy<List<FlickrPhoto>> strategy) {
        this.service = service;
        this.strategy = strategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.strategy.toSAX(this.service.getRandomPhotos(RANDOM_PHOTO_COUNT), xmlConsumer);
    }
}
