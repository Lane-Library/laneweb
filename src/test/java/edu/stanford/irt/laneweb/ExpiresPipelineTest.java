package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.components.pipeline.OutputComponentSelector;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.reading.Reader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpiresPipelineTest {

    private ExpiresPipeline pipeline;

    private Environment environment;

    private Parameters params;

    private Reader reader;

    private OutputComponentSelector selector;

    private ComponentManager manager;

    private Map objectModel;

    private Cache cache;

    @Before
    public void setUp() throws Exception {
        this.pipeline = new ExpiresPipeline();
        this.environment = createMock(Environment.class);
        this.params = createMock(Parameters.class);
        expect(this.params.getParameter("expires", null)).andReturn(null);
        expect(this.params.getParameterAsInteger("outputBufferSize", -1)).andReturn(new Integer(-1));
        expect(this.params.getParameter("cache-role", "org.apache.cocoon.caching.Cache")).andReturn("org.apache.cocoon.caching.Cache");
        expect(this.params.getParameterAsLong("cache-expires", 3600)).andReturn(new Long(3600));
        expect(this.params.getParameter("expires", null)).andReturn(null);
        expect(this.params.getParameterAsInteger("outputBufferSize", -1)).andReturn(new Integer(-1));
        expect(this.params.getParameterAsLong("cache-expires", 3600)).andReturn(new Long(3600));
        expect(this.params.getParameter("cache-key", null)).andReturn(null);
        replay(this.params);
        this.reader = createMock(Reader.class);
        expect(this.reader.getMimeType()).andReturn(null);
        expect(this.reader.shouldSetContentLength()).andReturn(Boolean.FALSE);
        this.reader.setOutputStream(isA(OutputStream.class));
        this.reader.generate();
        replay(this.reader);
        this.selector = createMock(OutputComponentSelector.class);
        expect(this.selector.select(null)).andReturn(this.reader);
        expect(this.selector.getMimeTypeForHint(null)).andReturn(null);
        replay(this.selector);
        this.cache = createNiceMock(Cache.class);
        replay(this.cache);
        this.manager = createMock(ComponentManager.class);
        expect(this.manager.lookup("org.apache.cocoon.caching.Cache")).andReturn(this.cache);
        expect(this.manager.lookup(Reader.ROLE + "Selector")).andReturn(this.selector);
        replay(this.manager);
        this.objectModel = createMock(Map.class);
        expect(this.objectModel.get("org.apache.cocoon.components.pipeline.impl.ExpiresCachingProcessingPipeline/Expires")).andReturn(null);
        expect(this.objectModel.get("org.apache.cocoon.components.pipeline.impl.ExpiresCachingProcessingPipeline/CacheKey"))
                .andReturn(null);
        replay(this.objectModel);
        this.pipeline.compose(this.manager);
        this.pipeline.enableLogging(createMock(Logger.class));

    }

    @After
    public void tearDown() throws Exception {
        verify(this.reader);
        verify(this.selector);
        verify(this.manager);
        verify(this.cache);
    }

    @Test
    public void testProcessReaderEnvironment() throws ProcessingException, ParameterException, IOException {
        expect(this.environment.getObjectModel()).andReturn(this.objectModel);
        expect(this.environment.getURIPrefix()).andReturn("foo");
        expect(this.environment.getURI()).andReturn("bar");
        expect(this.environment.getOutputStream(-1)).andReturn(null);
        expect(this.environment.getContentType()).andReturn(null);
        replay(this.environment);
        this.pipeline.parameterize(this.params);
        this.pipeline.setup(this.params);
        this.pipeline.setReader(null, null, this.params, null);
        this.pipeline.processReader(this.environment);
        verify(this.params);
        verify(this.environment);
    }

}
