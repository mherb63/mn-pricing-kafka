package com.mherb.mnaut;

import com.mherb.mnaut.configuration.ExternalQuoteProducer;
import com.mherb.mnaut.domain.ExternalQuote;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@Slf4j
class TestExternalQuoteProducer {

    private static final String PROPERTY_NAME = "TestExternalQuoteProducer";

    @Rule
    public static KafkaContainer kafka = new KafkaContainer(DockerTestImages.KAFKA);

    private static ApplicationContext context;

    @BeforeAll
    static void start() {
        kafka.start();
        log.debug("Bootstrap Servers: {}", kafka.getBootstrapServers());

        context = ApplicationContext.run(
                CollectionUtils.mapOf(
                        "kafka.bootstrap.servers", kafka.getBootstrapServers(),
                        PROPERTY_NAME, StringUtils.TRUE
                ),
                Environment.TEST
        );
    }

    @AfterAll
    static void stop() {
        kafka.stop();
        context.close();
    }

    @Test
    void producing10RecordsWorks() {
        final ExternalQuoteObserver observer = context.getBean(ExternalQuoteObserver.class);
        final ExternalQuoteProducer producer = context.getBean(ExternalQuoteProducer.class);
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        IntStream.range(0, 10).forEach(count -> {
            String key = "TEST-" + count;
            ExternalQuote externalQuote = new ExternalQuote(key, randomValue(random), randomValue(random));
            producer.send(key, externalQuote);

            log.debug("Producer sent {}", externalQuote);
        });
        Awaitility.await().untilAsserted(() -> {
            assertEquals(10, observer.inspected.size());
        });
    }

    private BigDecimal randomValue(final ThreadLocalRandom random) {
        return BigDecimal.valueOf(random.nextDouble(0, 1000));
    }

    @KafkaListener(
            clientId = "external-quote-observer",
            offsetReset = OffsetReset.EARLIEST
    )
    @Requires(env = Environment.TEST)
    @Requires(property = PROPERTY_NAME, value = StringUtils.TRUE)
    public static class ExternalQuoteObserver {

        List<ExternalQuote> inspected = new ArrayList<>();

        @Topic("external-quotes")
        void receive(List<ExternalQuote> externalQuotes) {
            log.debug("Consumed: {}", externalQuotes);
            inspected.addAll(externalQuotes);
        }
    }

}
