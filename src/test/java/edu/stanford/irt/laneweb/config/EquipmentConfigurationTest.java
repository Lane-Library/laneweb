package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;

public class EquipmentConfigurationTest {

    private EquipmentConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new EquipmentConfiguration();
    }

    @Test
    public void testEquipmentGenerator() throws BeansException {
        assertNotNull(this.configuration.equipmentListGenerator(null));
    }

    @Test
    public void testRestEquipmentService() {
        assertNotNull(this.configuration.restEquipmentService(null, null));
    }
}
