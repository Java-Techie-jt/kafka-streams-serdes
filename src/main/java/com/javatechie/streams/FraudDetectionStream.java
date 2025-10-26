package com.javatechie.streams;

import com.javatechie.events.Transaction;
import com.javatechie.serdes.TransactionSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
@Slf4j
public class FraudDetectionStream {

    //create bean
    //-> read the topic
    //-> process filter
    //-> write to dest

    @Bean
    public KStream<String, Transaction> fraudDetectStream(StreamsBuilder builder) {

       // var transactionSerde = new JsonSerde<>(Transaction.class);

        KStream<String, Transaction> stream =
                builder.stream("transactions", Consumed.with(Serdes.String(), new TransactionSerde()));

        stream
                .filter((key, tx) -> tx.amount() > 10000)
                .peek((key, tx) -> log.warn("⚠️ FRAUD ALERT for {}", tx))
                .to("fraud-alerts", Produced.with(Serdes.String(), new TransactionSerde()));

        return stream;

    }


}
