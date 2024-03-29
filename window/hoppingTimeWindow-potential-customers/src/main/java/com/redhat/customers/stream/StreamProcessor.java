package com.redhat.customers.stream;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.streams.StreamsConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;
import java.util.Random;

public abstract class StreamProcessor {
    private final Random random = new Random();

    @ConfigProperty(name = "quarkus.kafka-streams.bootstrap-servers")
    String bootstrapServers;



    protected Properties generateStreamConfig() {
        Properties props = new Properties();

        props.put(
                StreamsConfig.APPLICATION_ID_CONFIG,
                this.getClass().getSimpleName() + random.nextInt()
        );

        props.put(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        return props;
    }
}
