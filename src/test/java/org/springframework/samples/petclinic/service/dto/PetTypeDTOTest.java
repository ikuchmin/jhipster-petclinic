package org.springframework.samples.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class PetTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetTypeDTO.class);
        PetTypeDTO petTypeDTO1 = new PetTypeDTO();
        petTypeDTO1.setId(1L);
        PetTypeDTO petTypeDTO2 = new PetTypeDTO();
        assertThat(petTypeDTO1).isNotEqualTo(petTypeDTO2);
        petTypeDTO2.setId(petTypeDTO1.getId());
        assertThat(petTypeDTO1).isEqualTo(petTypeDTO2);
        petTypeDTO2.setId(2L);
        assertThat(petTypeDTO1).isNotEqualTo(petTypeDTO2);
        petTypeDTO1.setId(null);
        assertThat(petTypeDTO1).isNotEqualTo(petTypeDTO2);
    }
}
