package com.redhat.customers.event;

public class PotentialCustomersWereDetected {
    public String num;
    public Long timestamp;

    public PotentialCustomersWereDetected() {}

    public PotentialCustomersWereDetected(String num, Integer timestamp) {
        this.num = num;
        this.timestamp = timestamp;
    }
}
