package org.springframework.samples.petclinic.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Speciality getSpecialitySample1() {
        return new Speciality().id(1L).name("name1");
    }

    public static Speciality getSpecialitySample2() {
        return new Speciality().id(2L).name("name2");
    }

    public static Speciality getSpecialityRandomSampleGenerator() {
        return new Speciality().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
