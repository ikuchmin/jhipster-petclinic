package org.springframework.samples.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.samples.petclinic.domain.SpecialityTestSamples.*;
import static org.springframework.samples.petclinic.domain.VetTestSamples.*;
import static org.springframework.samples.petclinic.domain.VisitTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class VetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vet.class);
        Vet vet1 = getVetSample1();
        Vet vet2 = new Vet();
        assertThat(vet1).isNotEqualTo(vet2);

        vet2.setId(vet1.getId());
        assertThat(vet1).isEqualTo(vet2);

        vet2 = getVetSample2();
        assertThat(vet1).isNotEqualTo(vet2);
    }

    @Test
    void visitsTest() throws Exception {
        Vet vet = getVetRandomSampleGenerator();
        Visit visitBack = getVisitRandomSampleGenerator();

        vet.addVisits(visitBack);
        assertThat(vet.getVisits()).containsOnly(visitBack);
        assertThat(visitBack.getVet()).isEqualTo(vet);

        vet.removeVisits(visitBack);
        assertThat(vet.getVisits()).doesNotContain(visitBack);
        assertThat(visitBack.getVet()).isNull();

        vet.visits(new HashSet<>(Set.of(visitBack)));
        assertThat(vet.getVisits()).containsOnly(visitBack);
        assertThat(visitBack.getVet()).isEqualTo(vet);

        vet.setVisits(new HashSet<>());
        assertThat(vet.getVisits()).doesNotContain(visitBack);
        assertThat(visitBack.getVet()).isNull();
    }

    @Test
    void specialitiesTest() throws Exception {
        Vet vet = getVetRandomSampleGenerator();
        Speciality specialityBack = getSpecialityRandomSampleGenerator();

        vet.addSpecialities(specialityBack);
        assertThat(vet.getSpecialities()).containsOnly(specialityBack);

        vet.removeSpecialities(specialityBack);
        assertThat(vet.getSpecialities()).doesNotContain(specialityBack);

        vet.specialities(new HashSet<>(Set.of(specialityBack)));
        assertThat(vet.getSpecialities()).containsOnly(specialityBack);

        vet.setSpecialities(new HashSet<>());
        assertThat(vet.getSpecialities()).doesNotContain(specialityBack);
    }
}
