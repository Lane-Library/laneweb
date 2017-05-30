package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public class EquipmentConfigurationTest {

    private BeanFactory beanFactory;

    private EquipmentConfiguration configuration;

    @Before
    public void setUp() {
        this.beanFactory = createMock(BeanFactory.class);
        this.configuration = new EquipmentConfiguration(null, this.beanFactory, null);
    }

    @Test
    public void testEquipmentGenerator() throws BeansException, IOException {
        assertNotNull(this.configuration.equipmentGenerator());
    }

    @Test
    public void testEquipmentStatusTransformer() throws IOException {
        assertNotNull(this.configuration.equipmentStatusTransformer());
    }
}
