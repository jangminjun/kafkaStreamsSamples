package com.redhat.customers.stream;

import com.redhat.customers.event.PotentialCustomersWereDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.jboss.logging.Logger;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.streams.StreamsConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.time.Duration;

@ApplicationScoped
public class TumblingWindows  {
    private static final Logger LOGGER = Logger.getLogger(TumblingWindows.class);

    // Reading topic
    static final String POTENTIAL_CUSTOMERS_TOPIC = "potential-customers-detected";
    static final int WINDOW_SIZE = 10;
    private final Random random = new Random();
    protected Properties generateStreamConfig() {
        Properties props = new Properties();

        props.put(
                StreamsConfig.APPLICATION_ID_CONFIG,
                this.getClass().getSimpleName() + random.nextInt()
        );

        props.put(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                "my-cluster-kafka-bootstrap.efnalf-kafka-cluster.svc:9092"
        );

        return props;
    }

    @Produces
    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<PotentialCustomersWereDetected> customersEventSerde
                = new ObjectMapperSerde<>(PotentialCustomersWereDetected.class);

        // TODO: Build the stream topology
        builder.stream(
                POTENTIAL_CUSTOMERS_TOPIC,
                Consumed.with(Serdes.String(), customersEventSerde)
        ).groupByKey()
            .windowedBy(
                TimeWindows.of(Duration.ofSeconds(WINDOW_SIZE))
                        .grace(Duration.ofSeconds(12))
        ).count()
        .toStream()
        .print(Printed.toSysOut());

        streams = new KafkaStreams(
                builder.build(),
                generateStreamConfig()
        );

        streams.start();
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        streams.close();
    }
}
