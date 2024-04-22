package org.springframework.samples.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PetCriteriaTest {

    @Test
    void newPetCriteriaHasAllFiltersNullTest() {
        var petCriteria = new PetCriteria();
        assertThat(petCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void petCriteriaFluentMethodsCreatesFiltersTest() {
        var petCriteria = new PetCriteria();

        setAllFilters(petCriteria);

        assertThat(petCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void petCriteriaCopyCreatesNullFilterTest() {
        var petCriteria = new PetCriteria();
        var copy = petCriteria.copy();

        assertThat(petCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(petCriteria)
        );
    }

    @Test
    void petCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var petCriteria = new PetCriteria();
        setAllFilters(petCriteria);

        var copy = petCriteria.copy();

        assertThat(petCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(petCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var petCriteria = new PetCriteria();

        assertThat(petCriteria).hasToString("PetCriteria{}");
    }

    private static void setAllFilters(PetCriteria petCriteria) {
        petCriteria.id();
        petCriteria.name();
        petCriteria.birthDate();
        petCriteria.visitsId();
        petCriteria.typeId();
        petCriteria.ownerId();
        petCriteria.distinct();
    }

    private static Condition<PetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getVisitsId()) &&
                condition.apply(criteria.getTypeId()) &&
                condition.apply(criteria.getOwnerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PetCriteria> copyFiltersAre(PetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getVisitsId(), copy.getVisitsId()) &&
                condition.apply(criteria.getTypeId(), copy.getTypeId()) &&
                condition.apply(criteria.getOwnerId(), copy.getOwnerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
