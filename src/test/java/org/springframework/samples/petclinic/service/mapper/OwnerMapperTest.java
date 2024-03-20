package org.springframework.samples.petclinic.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class OwnerMapperTest {

    private OwnerMapper ownerMapper;

    @BeforeEach
    public void setUp() {
        ownerMapper = new OwnerMapperImpl();
    }
}
