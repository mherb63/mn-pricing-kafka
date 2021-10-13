package com.mherb.mnaut.service;

import com.mherb.mnaut.configuration.ExternalQuoteProducer;
import com.mherb.mnaut.domain.ExternalQuote;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
@Slf4j
@Requires(notEnv = Environment.TEST)
public class EventScheduler {

    private static final List<String> SYMBOLS = Arrays.asList("APPL", "AMZN", "GOOG", "FB", "TSLA", "MSFT", "NFLX");
    private final ExternalQuoteProducer externalQuoteProducer;

    public EventScheduler(ExternalQuoteProducer externalQuoteProducer) {
        this.externalQuoteProducer = externalQuoteProducer;
    }

    @Scheduled(fixedDelay = "10s")
    public void generate() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        ExternalQuote quote = ExternalQuote.builder()
                .symbol(SYMBOLS.get(random.nextInt(0, SYMBOLS.size() - 1)))
                .lastPrice(randomValue(random))
                .volume(randomValue(random))
                .build();

        log.debug("Generate external quote: " + quote);

        externalQuoteProducer.send(quote.getSymbol(), quote);
    }

    private BigDecimal randomValue(final ThreadLocalRandom random) {
        return BigDecimal.valueOf((random.nextDouble(0, 1000)));
    }
}
