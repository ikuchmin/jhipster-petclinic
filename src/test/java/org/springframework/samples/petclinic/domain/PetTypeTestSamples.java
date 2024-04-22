package org.springframework.samples.petclinic.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PetTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PetType getPetTypeSample1() {
        return new PetType().id(1L).name("name1");
    }

    public static PetType getPetTypeSample2() {
        return new PetType().id(2L).name("name2");
    }

    public static PetType getPetTypeRandomSampleGenerator() {
        return new PetType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
