package com.redhat.telemetry.producer;

import java.time.Duration;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ProducerApp {

    private static final Logger LOG = Logger.getLogger(ProducerApp.class);

    private final Random random = new Random();

    // TODO: Implement the Kafka producer
    @Outgoing("payments")
    public Multi<Record<String, Integer>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onOverflow().drop()
                .map(tick -> {
                    String currentPayment = "Payment-" + random.nextInt(10);
                    int currentMeasure = random.nextInt(100);

                    LOG.infov("Payment ID: {0}, measure: {1}",
                            currentPayment,
                            currentMeasure
                    );

                    return Record.of(currentPayment, currentMeasure);
                });
    }
}
