package com.mherb.mnaut.configuration;

import com.mherb.mnaut.domain.ExternalQuote;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient(id = "mn-external-quote-producer")
public interface ExternalQuoteProducer {

    @Topic("external-quotes")
    void send(@KafkaKey String symbol, ExternalQuote externalQuote);
}
