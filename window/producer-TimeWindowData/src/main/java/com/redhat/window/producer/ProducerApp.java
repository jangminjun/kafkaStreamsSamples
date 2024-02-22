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
    public Multi<Record<String, Long>> generate() {

        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onOverflow().drop()
                .map(tick -> {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    long currentTime = timestamp.getTime();
                    num++;

                    if(num % 10 ==0)
                    {

                        LOG.infov("Event sent : {0}, {1} - (late)", num, currentTime);
                        System.out.println("before : " + currentTime);
                        currentTime = currentTime - 2000;
                        System.out.println("after" + currentTime);
                        num=0;
                        try{
                            Thread.sleep(5000);
                        } catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        return Record.of(String.valueOf(num), currentTime);

                    } else{
                        LOG.infov("Event sent : {0}, {1} - (on time)", num, currentTime);
                    }
                    return Record.of(String.valueOf(num), currentTime);
                });
    }
}
