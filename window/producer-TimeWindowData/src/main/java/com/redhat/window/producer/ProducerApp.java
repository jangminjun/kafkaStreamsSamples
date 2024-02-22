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
    Integer num=0;

    // TODO: Implement the Kafka producer
    @Outgoing("potential-customers-detected")
    public Multi<Record<Integer, Long>> generate() {

        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onOverflow().drop()
                .map(tick -> {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long currentTime = timestamp.getTime();
                    num++;
                    LOG.infov("Event sent : {0}, measure: {1}",
                            num,
                            currentTime
                    );
                    if(num % 10 ==0)
                    {

                        LOG.infov("Event sent : {0} - {1} late",
                                num, currentTime
                        );

                        currentTime = currentTime - 1100;
                        num=0;
                        try{
                            Thread.sleep(5000);
                        } catch(InterruptedException e){
                            e.printStackTrace();
                        }

                    }
                    return Record.of(num, currentTime);
                });
    }
}
