package org.springframework.samples.petclinic.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class VetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VetDTO.class);
        VetDTO vetDTO1 = new VetDTO();
        vetDTO1.setId(1L);
        VetDTO vetDTO2 = new VetDTO();
        assertThat(vetDTO1).isNotEqualTo(vetDTO2);
        vetDTO2.setId(vetDTO1.getId());
        assertThat(vetDTO1).isEqualTo(vetDTO2);
        vetDTO2.setId(2L);
        assertThat(vetDTO1).isNotEqualTo(vetDTO2);
        vetDTO1.setId(null);
        assertThat(vetDTO1).isNotEqualTo(vetDTO2);
    }
}
