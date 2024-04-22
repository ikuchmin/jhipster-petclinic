package org.springframework.samples.petclinic.service.mapper;

import static org.springframework.samples.petclinic.domain.SpecialityAsserts.*;
import static org.springframework.samples.petclinic.domain.SpecialityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialityMapperTest {

    private SpecialityMapper specialityMapper;

    @BeforeEach
    void setUp() {
        specialityMapper = new SpecialityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSpecialitySample1();
        var actual = specialityMapper.toEntity(specialityMapper.toDto(expected));
        assertSpecialityAllPropertiesEquals(expected, actual);
    }
}
