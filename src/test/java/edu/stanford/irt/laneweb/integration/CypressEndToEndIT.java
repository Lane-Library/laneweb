package edu.stanford.irt.laneweb.integration;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    private static final String DOCKER_IMAGE = "cypress/included:13.15.2";

    private static final int MAX_TOTAL_TEST_TIME_IN_MINUTES = 5;

    @LocalServerPort
    private int port;

    @Test
    public void runCypressTests() throws InterruptedException {
        Testcontainers.exposeHostPorts(this.port);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try (GenericContainer<?> container = createCypressContainer()) {
            container.start();
            container.followOutput(new Consumer<OutputFrame>() {

                @Override
                public void accept(final OutputFrame outputFrame) {
                    // stream output to console, but strip a little extra whitespace
                    if (!outputFrame.getUtf8String().equals("\n")){
                        System.out.print(outputFrame.getUtf8String());
                    }
                    // brittle test to determine if tests have failed
                    if (outputFrame.getUtf8String().contains(" failed (")) {
                        countDownLatch.countDown();
                        fail("Failure while running Cypress tests");
                    } else if (outputFrame.getUtf8String().contains("All specs passed!")) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await(MAX_TOTAL_TEST_TIME_IN_MINUTES, TimeUnit.MINUTES);
        }
    }

    @NotNull
    private GenericContainer<?> createCypressContainer() {
        GenericContainer<?> result = new GenericContainer<>(DOCKER_IMAGE);
        result.withClasspathResourceMapping("e2e", "/e2e", BindMode.READ_WRITE);
        result.withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("bash", "-c",
                "npm install && chmod a+wx -R node_modules && cypress run --headless"));
        result.setWorkingDirectory("/e2e");
        result.addEnv("CYPRESS_baseUrl", "http://host.testcontainers.internal:" + this.port);
        return result;
    }
}
