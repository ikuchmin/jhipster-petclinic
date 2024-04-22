package org.springframework.samples.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PetAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPetAllPropertiesEquals(Pet expected, Pet actual) {
        assertPetAutoGeneratedPropertiesEquals(expected, actual);
        assertPetAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPetAllUpdatablePropertiesEquals(Pet expected, Pet actual) {
        assertPetUpdatableFieldsEquals(expected, actual);
        assertPetUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPetAutoGeneratedPropertiesEquals(Pet expected, Pet actual) {
        assertThat(expected)
            .as("Verify Pet auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPetUpdatableFieldsEquals(Pet expected, Pet actual) {
        assertThat(expected)
            .as("Verify Pet relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getBirthDate()).as("check birthDate").isEqualTo(actual.getBirthDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPetUpdatableRelationshipsEquals(Pet expected, Pet actual) {
        assertThat(expected)
            .as("Verify Pet relationships")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getOwner()).as("check owner").isEqualTo(actual.getOwner()));
    }
}
