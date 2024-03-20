package org.springframework.samples.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.samples.petclinic.domain.SpecialityTestSamples.*;
import static org.springframework.samples.petclinic.domain.VetTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class SpecialityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Speciality.class);
        Speciality speciality1 = getSpecialitySample1();
        Speciality speciality2 = new Speciality();
        assertThat(speciality1).isNotEqualTo(speciality2);

        speciality2.setId(speciality1.getId());
        assertThat(speciality1).isEqualTo(speciality2);

        speciality2 = getSpecialitySample2();
        assertThat(speciality1).isNotEqualTo(speciality2);
    }

    @Test
    void vetTest() throws Exception {
        Speciality speciality = getSpecialityRandomSampleGenerator();
        Vet vetBack = getVetRandomSampleGenerator();

        speciality.addVet(vetBack);
        assertThat(speciality.getVets()).containsOnly(vetBack);
        assertThat(vetBack.getSpecialities()).containsOnly(speciality);

        speciality.removeVet(vetBack);
        assertThat(speciality.getVets()).doesNotContain(vetBack);
        assertThat(vetBack.getSpecialities()).doesNotContain(speciality);

        speciality.vets(new HashSet<>(Set.of(vetBack)));
        assertThat(speciality.getVets()).containsOnly(vetBack);
        assertThat(vetBack.getSpecialities()).containsOnly(speciality);

        speciality.setVets(new HashSet<>());
        assertThat(speciality.getVets()).doesNotContain(vetBack);
        assertThat(vetBack.getSpecialities()).doesNotContain(speciality);
    }
}
