package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class EresourcesGeneratorTest {

    private EresourcesGenerator generator;

    private ServiceManager serviceManager;

    private Map<String, Object> objectModel;

    private Parameters parameters;

    private CollectionManager collectionManager;

    private Request request;

    @Before
    public void setUp() throws Exception {
        this.generator = new EresourcesGenerator();
        this.serviceManager = createMock(ServiceManager.class);
        this.objectModel = new HashMap<String, Object>();
        this.parameters = createMock(Parameters.class);
        this.collectionManager = createMock(CollectionManager.class);
        this.request = createMock(Request.class);
        this.objectModel.put("request", this.request);
    }

    @Test
    public void testRecycle() {
        this.generator.recycle();
    }

    @Test
    public void testDispose() throws ServiceException, ParameterException {
        expect(this.parameters.getParameter("collection", "laneconnex"))
                .andReturn("laneconnex");
        replay(this.parameters);
        expect(
                this.serviceManager
                        .lookup("edu.stanford.irt.eresources.CollectionManager/laneconnex"))
                .andReturn(this.collectionManager);
        this.serviceManager.release(this.collectionManager);
        replay(this.serviceManager);
        this.generator.parameterize(this.parameters);
        this.generator.service(this.serviceManager);
        this.generator.initialize();
        this.generator.dispose();
        verify(this.serviceManager);
        verify(this.parameters);
    }

    @Test
    public void testSetupSourceResolverMapStringParameters()
            throws ProcessingException, SAXException, IOException,
            ParameterException {
        expect(this.parameters.getParameter("mode", "browse")).andReturn(
                "browse");
        replay(this.parameters);
        expect(this.request.getParameter("q")).andReturn(null);
        expect(this.request.getParameter("t")).andReturn("ej");
        expect(this.request.getParameter("s")).andReturn(null);
        expect(this.request.getParameter("a")).andReturn("z");
        expect(this.request.getParameter("m")).andReturn(null);
        replay(this.request);
        this.generator.setup(null, this.objectModel, null, this.parameters);
        verify(this.request);
        verify(this.parameters);
    }

    @Test
    public void testGenerate() throws ServiceException, ParameterException, ProcessingException, SAXException, IOException {
        expect(this.parameters.getParameter("collection", "laneconnex"))
                .andReturn("laneconnex");
        expect(
                this.serviceManager
                        .lookup("edu.stanford.irt.eresources.CollectionManager/laneconnex"))
                .andReturn(this.collectionManager);
        replay(this.serviceManager);
        expect(this.collectionManager.getType("ej", 'z')).andReturn(
                new LinkedList<Eresource>());
        replay(this.collectionManager);
        expect(this.parameters.getParameter("mode", "browse")).andReturn(
                "browse");
        replay(this.parameters);
        expect(this.request.getParameter("q")).andReturn(null);
        expect(this.request.getParameter("t")).andReturn("ej");
        expect(this.request.getParameter("s")).andReturn(null);
        expect(this.request.getParameter("a")).andReturn("z");
        expect(this.request.getParameter("m")).andReturn(null);
        replay(this.request);
        this.generator.parameterize(this.parameters);
        this.generator.service(this.serviceManager);
        this.generator.initialize();
        this.generator.setConsumer(createMock(XMLConsumer.class));
        this.generator.setup(null, this.objectModel, null, this.parameters);
        this.generator.generate();
        verify(this.request);
        verify(this.parameters);
        verify(this.collectionManager);
        verify(this.serviceManager);
    }

    @Test
    public void testGetEresourceList() {
        try {
            this.generator.getEresourceList();
            fail("no IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

}
