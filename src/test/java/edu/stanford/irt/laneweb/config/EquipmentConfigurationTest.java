package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;

public class EquipmentConfigurationTest {

    private EquipmentConfiguration configuration;

    @BeforeEach
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
