package org.springframework.samples.petclinic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.config.AsyncSyncConfiguration;
import org.springframework.samples.petclinic.config.EmbeddedKafka;
import org.springframework.samples.petclinic.config.EmbeddedSQL;
import org.springframework.samples.petclinic.config.JacksonConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { PetClinicApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
