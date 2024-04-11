package org.springframework.samples.petclinic.broker;

import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements Supplier<String> {

    public static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Override
    public String get() {
        logger.info("Producing message");

        return "kakfa_producer";
    }
}
