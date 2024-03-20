package org.springframework.samples.petclinic.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class SpecialityMapperTest {

    private SpecialityMapper specialityMapper;

    @BeforeEach
    public void setUp() {
        specialityMapper = new SpecialityMapperImpl();
    }
}
