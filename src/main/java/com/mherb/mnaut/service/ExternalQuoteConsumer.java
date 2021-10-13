package com.mherb.mnaut.service;

import com.mherb.mnaut.configuration.PriceUpdateProducer;
import com.mherb.mnaut.domain.ExternalQuote;
import com.mherb.mnaut.domain.PriceUpdate;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@KafkaListener(clientId = "mn-external-quote-consumer", groupId = "external-quote-consumer", batch = true, offsetReset = OffsetReset.EARLIEST)
@Slf4j
public class ExternalQuoteConsumer {

    private final PriceUpdateProducer priceUpdateProducer;

    public ExternalQuoteConsumer(PriceUpdateProducer priceUpdateProducer) {
        this.priceUpdateProducer = priceUpdateProducer;
    }

    @Topic("external-quotes")
    void receive(List<ExternalQuote> externalQuotes) {
        log.debug("Consuming batch of external quotes {}", externalQuotes);

        // forward price updates
        final List<PriceUpdate> priceUpdates = externalQuotes.stream().map(q -> new PriceUpdate(q.getSymbol(), q.getLastPrice())).collect(Collectors.toList());
        priceUpdateProducer.send(priceUpdates)
            .doOnError(e -> log.error(e.getMessage(), e))
            .doOnNext(recordMetadata ->
                log.debug("Record sent to topic {} on offset {}", recordMetadata.topic(), recordMetadata.offset())
            )
            .subscribe();
    }
}
