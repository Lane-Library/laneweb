package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

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
    public void testEquipmentGenerator() throws BeansException, IOException {
        assertNotNull(this.configuration.equipmentGenerator(null, null));
    }

    @Test
    public void testEquipmentStatusTransformer() throws IOException {
        assertNotNull(this.configuration.equipmentStatusTransformer(null));
    }
}
