package com.redhat.telemetry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/largePayments")
public class LargePaymentsResource {

    @Inject
    LargePaymentsConsumer consumer;

    @GET
    public LargePaymentsStats getLargePaymentsStats() {
        return consumer.stats;
    }
}
