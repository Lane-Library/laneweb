package edu.stanford.irt.laneweb.history;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class HistoryPhotoGeneratorTest {

    private HistoryPhotoGenerator generator;

    private HistoryPhotoListService service;

    private SAXStrategy<List<HistoryPhoto>> strategy;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.service = mock(HistoryPhotoListService.class);
        this.strategy = mock(SAXStrategy.class);
        this.generator = new HistoryPhotoGenerator(this.service, this.strategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() {
        expect(this.service.getRandomPhotos(12)).andReturn(Collections.emptyList());
        this.strategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.service, this.strategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.strategy, this.xmlConsumer);
    }
}
