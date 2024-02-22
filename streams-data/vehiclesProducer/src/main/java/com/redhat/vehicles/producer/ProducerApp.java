package com.redhat.vehicles.producer;

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
    @Outgoing("vehicle-positions")
    public Multi<Record<String, Integer>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onOverflow().drop()
                .map(tick -> {
                    String empID = "Payment-" + random.nextInt(20);
                    int currentPayment = random.nextInt(2000);

                    LOG.infov("Employee ID: {0}, measure: {1}",
                            empID,
                            currentPayment
                    );

                    return Record.of(empID, currentPayment);
                });
    }
}
