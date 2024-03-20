package org.springframework.samples.petclinic.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PetMapperTest {

    private PetMapper petMapper;

    @BeforeEach
    public void setUp() {
        petMapper = new PetMapperImpl();
    }
}
