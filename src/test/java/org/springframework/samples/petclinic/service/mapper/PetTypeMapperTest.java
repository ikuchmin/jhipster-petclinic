package org.springframework.samples.petclinic.service.mapper;

import static org.springframework.samples.petclinic.domain.PetTypeAsserts.*;
import static org.springframework.samples.petclinic.domain.PetTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PetTypeMapperTest {

    private PetTypeMapper petTypeMapper;

    @BeforeEach
    void setUp() {
        petTypeMapper = new PetTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPetTypeSample1();
        var actual = petTypeMapper.toEntity(petTypeMapper.toDto(expected));
        assertPetTypeAllPropertiesEquals(expected, actual);
    }
}
