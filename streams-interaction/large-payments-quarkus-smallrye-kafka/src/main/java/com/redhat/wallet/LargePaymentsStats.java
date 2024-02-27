package com.redhat.wallet;

class LargePaymentsStats {
    public float latest = 0;
    public int count = 0;
    public float sum = 0;
    public float average = 0;

    public void add(int largePaymentsValue) {
        float normalizedValue = (float) largePaymentsValue;
        count++;
        latest = normalizedValue;
        sum += normalizedValue;
        average = sum / count;
    }
}
