package com.mherb.mnaut.configuration;

import com.mherb.mnaut.domain.PriceUpdate;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Flux;

import java.util.List;

@KafkaClient(batch = true)
public interface PriceUpdateProducer {

    @Topic("price-update")
    Flux<RecordMetadata> send(List<PriceUpdate> prices);
}
