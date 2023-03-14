package edu.stanford.irt.laneweb.history;

import java.util.List;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class HistoryPhotoGenerator extends AbstractGenerator {

    private static final int RANDOM_PHOTO_COUNT = 12;

    private HistoryPhotoListService service;

    private SAXStrategy<List<HistoryPhoto>> strategy;

    public HistoryPhotoGenerator(final HistoryPhotoListService service,
            final SAXStrategy<List<HistoryPhoto>> strategy) {
        this.service = service;
        this.strategy = strategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.strategy.toSAX(this.service.getRandomPhotos(RANDOM_PHOTO_COUNT), xmlConsumer);
    }
}
