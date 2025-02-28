package edu.stanford.irt.laneweb.integration;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;

import edu.stanford.irt.laneweb.Laneweb;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Laneweb.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CypressEndToEndIT {

    private static final String DOCKER_IMAGE = "cypress/included:14.1.0";

    private static final String INSTRUMENTED_FILES_DIR = "target/test-classes/e2e/coverage";

    @Value("${cypress.tests.disable:false}")
    private boolean disableTests;

    @Value("${cypress.tests.maxTimeInMinutes:10}")
    private int maxTestTimeInMinutes;

    @LocalServerPort
    private int port;

    @Test
    public void runCypressTests() throws InterruptedException {
        if (this.disableTests) {
            System.out.println("\nCypress tests are disabled ... skipping \n");
            return;
        }
        Testcontainers.exposeHostPorts(this.port);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try (GenericContainer<?> container = createCypressContainer()) {
            container.start();
            // array so we can modify it in the consumer
            final int[] failing = { 0 };
            container.followOutput(new Consumer<OutputFrame>() {

                @Override
                public void accept(final OutputFrame outputFrame) {
                    String line = outputFrame.getUtf8String();
                    // stream output to console, but strip extra whitespace
                    if (!line.equals("\n")) {
                        System.out.print(line);
                    }
                    if (line.contains("Failing:      ")) {
                        failing[0] += Integer.parseInt(line.split("Failing:")[1].trim().split("\\s+")[0]);
                    } else if (line.contains("ET VOILA!")) {
                        countDownLatch.countDown();
                    }
                }
            });
            boolean testsDidNotTimeOut = countDownLatch.await(this.maxTestTimeInMinutes, TimeUnit.MINUTES);
            if (testsDidNotTimeOut) {
                if (failing[0] > 0) {
                    fail("Failing Cypress tests: " + failing[0]);
                }
            } else {
                fail("Cypress tests did not complete in " + this.maxTestTimeInMinutes + " minutes");
            }
        }
    }

    @NotNull
    private GenericContainer<?> createCypressContainer() {
        GenericContainer<?> result = new GenericContainer<>(DOCKER_IMAGE);
        result.withClasspathResourceMapping("e2e", "/e2e", BindMode.READ_WRITE);
        result.withFileSystemBind(INSTRUMENTED_FILES_DIR, "/e2e/coverage", BindMode.READ_WRITE);
        result.withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("bash", "-c",
                "npm install && cypress run --headless; chmod -R 777 .; echo 'ET VOILA!'"));
        result.setWorkingDirectory("/e2e");
        result.addEnv("CYPRESS_baseUrl", "http://host.testcontainers.internal:" + this.port);
        result.addEnv("INSTRUMENTED_FILES_DIR", "/e2e/coverage");
        return result;
    }
}
