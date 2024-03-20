package org.springframework.samples.petclinic.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PetTypeMapperTest {

    private PetTypeMapper petTypeMapper;

    @BeforeEach
    public void setUp() {
        petTypeMapper = new PetTypeMapperImpl();
    }
}
