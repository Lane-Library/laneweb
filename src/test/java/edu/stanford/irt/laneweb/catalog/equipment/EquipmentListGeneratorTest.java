package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class EquipmentListGeneratorTest {

    private EquipmentListGenerator generator;

    private SAXStrategy<List<Equipment>> saxStrategy;

    private EquipmentService service;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.service = mock(EquipmentService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new EquipmentListGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() {
        expect(this.service.getList()).andReturn(Collections.emptyList());
        this.saxStrategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }
}
