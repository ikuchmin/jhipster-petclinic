package org.springframework.samples.petclinic.service.mapper;

import static org.springframework.samples.petclinic.domain.OwnerAsserts.*;
import static org.springframework.samples.petclinic.domain.OwnerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OwnerMapperTest {

    private OwnerMapper ownerMapper;

    @BeforeEach
    void setUp() {
        ownerMapper = new OwnerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOwnerSample1();
        var actual = ownerMapper.toEntity(ownerMapper.toDto(expected));
        assertOwnerAllPropertiesEquals(expected, actual);
    }
}
