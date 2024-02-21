package com.redhat.wallet;

import javax.inject.Singleton;

import org.eclipse.microprofile.reactive.messaging.Incoming;


@Singleton
public class LargePaymentsConsumer {

    public LargePaymentsStats stats = new LargePaymentsStats();

    @Incoming("large-payments")
    public void consume(int largePaymentsValue) {
        stats.add(largePaymentsValue);
        System.out.println("Received large-Payments value: " + largePaymentsValue);
    }

}
