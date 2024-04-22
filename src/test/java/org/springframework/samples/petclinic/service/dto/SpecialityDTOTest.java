package org.springframework.samples.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class SpecialityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialityDTO.class);
        SpecialityDTO specialityDTO1 = new SpecialityDTO();
        specialityDTO1.setId(1L);
        SpecialityDTO specialityDTO2 = new SpecialityDTO();
        assertThat(specialityDTO1).isNotEqualTo(specialityDTO2);
        specialityDTO2.setId(specialityDTO1.getId());
        assertThat(specialityDTO1).isEqualTo(specialityDTO2);
        specialityDTO2.setId(2L);
        assertThat(specialityDTO1).isNotEqualTo(specialityDTO2);
        specialityDTO1.setId(null);
        assertThat(specialityDTO1).isNotEqualTo(specialityDTO2);
    }
}
