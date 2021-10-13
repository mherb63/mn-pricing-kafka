package com.mherb.mnaut;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
class TestContainerSetup {

    @Rule
    public KafkaContainer kafka = new KafkaContainer(DockerTestImages.KAFKA);

    @Test
    void testSetup() {
        kafka.start();
        log.debug("Bootstrap Servers: {}", kafka.getBootstrapServers());
        kafka.stop();
    }

}
