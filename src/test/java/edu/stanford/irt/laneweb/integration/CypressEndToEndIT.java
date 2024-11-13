package edu.stanford.irt.laneweb.integration;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.Testcontainers;

import edu.stanford.irt.laneweb.Laneweb;
import io.github.wimdeblauwe.testcontainers.cypress.CypressContainer;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTestResults;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Laneweb.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CypressEndToEndIT {

    @LocalServerPort
    private int port;

    @Test
    public void runCypressTests() throws InterruptedException, IOException, TimeoutException {
        Testcontainers.exposeHostPorts(this.port);
        try (CypressContainer container = new CypressContainer()) {
            container.withLocalServerPort(this.port);
            container.start();
            CypressTestResults testResults = container.getTestResults();
            StringBuilder details = new StringBuilder();
            details.append(testResults.toString());
            details.append("\nDetails:");
            details.append("\n=========================================");
            testResults.getSuites().forEach(suite -> {
                details.append("\n");
                details.append("\nSuite: " + suite.getTitle());
                suite.getTests().forEach(test -> {
                    String status = test.isSuccess() ? "SUCCESS" : "FAILURE";
                    details.append("\n[TEST] " + test.getDescription() + ": " + status);
                });
            });
            if (testResults.getNumberOfFailingTests() > 0) {
                fail("Failure while running Cypress tests:\n" + details.toString() + "\n");
            } else {
                System.out.println("Cypress tests ran successfully:\n" + details.toString() + "\n");
            }
        }
    }
}