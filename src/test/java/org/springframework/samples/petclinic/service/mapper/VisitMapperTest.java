package org.springframework.samples.petclinic.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class VisitMapperTest {

    private VisitMapper visitMapper;

    @BeforeEach
    public void setUp() {
        visitMapper = new VisitMapperImpl();
    }
}
