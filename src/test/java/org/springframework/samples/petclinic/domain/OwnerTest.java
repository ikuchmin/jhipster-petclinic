package org.springframework.samples.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.samples.petclinic.domain.OwnerTestSamples.*;
import static org.springframework.samples.petclinic.domain.PetTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.rest.TestUtil;

class OwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = getOwnerSample1();
        Owner owner2 = new Owner();
        assertThat(owner1).isNotEqualTo(owner2);

        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);

        owner2 = getOwnerSample2();
        assertThat(owner1).isNotEqualTo(owner2);
    }

    @Test
    void petsTest() throws Exception {
        Owner owner = getOwnerRandomSampleGenerator();
        Pet petBack = getPetRandomSampleGenerator();

        owner.addPets(petBack);
        assertThat(owner.getPets()).containsOnly(petBack);
        assertThat(petBack.getOwner()).isEqualTo(owner);

        owner.removePets(petBack);
        assertThat(owner.getPets()).doesNotContain(petBack);
        assertThat(petBack.getOwner()).isNull();

        owner.pets(new HashSet<>(Set.of(petBack)));
        assertThat(owner.getPets()).containsOnly(petBack);
        assertThat(petBack.getOwner()).isEqualTo(owner);

        owner.setPets(new HashSet<>());
        assertThat(owner.getPets()).doesNotContain(petBack);
        assertThat(petBack.getOwner()).isNull();
    }
}
