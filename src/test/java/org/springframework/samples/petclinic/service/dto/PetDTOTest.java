package org.springframework.samples.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class PetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetDTO.class);
        PetDTO petDTO1 = new PetDTO();
        petDTO1.setId(1L);
        PetDTO petDTO2 = new PetDTO();
        assertThat(petDTO1).isNotEqualTo(petDTO2);
        petDTO2.setId(petDTO1.getId());
        assertThat(petDTO1).isEqualTo(petDTO2);
        petDTO2.setId(2L);
        assertThat(petDTO1).isNotEqualTo(petDTO2);
        petDTO1.setId(null);
        assertThat(petDTO1).isNotEqualTo(petDTO2);
    }
}
