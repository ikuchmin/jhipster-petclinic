package org.springframework.samples.petclinic.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class VetMapperTest {

    private VetMapper vetMapper;

    @BeforeEach
    public void setUp() {
        vetMapper = new VetMapperImpl();
    }
}
