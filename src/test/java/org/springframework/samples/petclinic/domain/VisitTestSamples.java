package org.springframework.samples.petclinic.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class VisitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Visit getVisitSample1() {
        return new Visit().id(1L);
    }

    public static Visit getVisitSample2() {
        return new Visit().id(2L);
    }

    public static Visit getVisitRandomSampleGenerator() {
        return new Visit().id(longCount.incrementAndGet());
    }
}
