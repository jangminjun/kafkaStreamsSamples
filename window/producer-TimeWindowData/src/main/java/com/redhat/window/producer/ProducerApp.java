package com.redhat.window.producer;

import java.time.Duration;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ProducerApp {

    private static final Logger LOG = Logger.getLogger(ProducerApp.class);


    // TODO: Implement the Kafka producer
    @Outgoing("potential-customers-detected")
    public Multi<Record<String, Integer>> generate() {
        int num=0;
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onOverflow().drop()
                .map(tick -> {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String currentTime = timestamp.getTime();
                    LOG.infov("Event sent : {0}, measure: {1}",
                            num++,
                            currentTime
                    );
                    if(num % 10 ==0)
                    {
                        num=0;
                        Thread.sleep(5);
                    }
                    return Record.of(num, currentTime);
                });
    }
}
