package org.springframework.samples.petclinic.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vet getVetSample1() {
        return new Vet().id(1L).firstName("firstName1").lastName("lastName1");
    }

    public static Vet getVetSample2() {
        return new Vet().id(2L).firstName("firstName2").lastName("lastName2");
    }

    public static Vet getVetRandomSampleGenerator() {
        return new Vet().id(longCount.incrementAndGet()).firstName(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString());
    }
}
