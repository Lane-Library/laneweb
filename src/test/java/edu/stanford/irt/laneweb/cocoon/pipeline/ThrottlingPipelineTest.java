package edu.stanford.irt.laneweb.cocoon.pipeline;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.transformation.Transformer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.SourceResolver;

public class ThrottlingPipelineTest {

    private BeanFactory beanFactory;

    private Environment environment;

    private Generator generator;

    private Parameters parameters;

    private ThrottlingPipeline pipeline;

    private SourceResolver sourceResolver;

    private Transformer transformer;

    @Before
    public void setUp() throws Exception {
        this.parameters = createMock(Parameters.class);
        this.environment = createMock(Environment.class);
        this.generator = createMock(Generator.class);
        this.beanFactory = createMock(BeanFactory.class);
        this.transformer = createMock(Transformer.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.pipeline = new ThrottlingPipeline(this.sourceResolver);
        this.pipeline.setBeanFactory(this.beanFactory);
    }

    @Test
    public void testProcessXMLPipelineEnvironment() throws ProcessingException, IOException, SAXException {
        expect(this.parameters.getParameter("request-key", null)).andReturn("foo");
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/foo"))
                .andReturn(this.transformer);
        this.generator.generate();
        this.environment.setContentType("text/xml");
        replay(this.environment, this.beanFactory, this.generator, this.transformer, this.parameters);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.addTransformer("foo", null, null, null);
        this.pipeline.setup(this.parameters);
        this.pipeline.processXMLPipeline(this.environment);
        verify(this.environment, this.beanFactory, this.generator, this.transformer, this.parameters);
    }

    @Test
    public void testSetupParameters() {
        expect(this.parameters.getParameter("request-key", null)).andReturn("foo");
        replay(this.parameters);
        this.pipeline.setup(this.parameters);
        verify(this.parameters);
    }
}
