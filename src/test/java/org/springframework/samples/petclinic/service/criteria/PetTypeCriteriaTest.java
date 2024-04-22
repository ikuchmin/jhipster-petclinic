package org.springframework.samples.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PetTypeCriteriaTest {

    @Test
    void newPetTypeCriteriaHasAllFiltersNullTest() {
        var petTypeCriteria = new PetTypeCriteria();
        assertThat(petTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void petTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var petTypeCriteria = new PetTypeCriteria();

        setAllFilters(petTypeCriteria);

        assertThat(petTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void petTypeCriteriaCopyCreatesNullFilterTest() {
        var petTypeCriteria = new PetTypeCriteria();
        var copy = petTypeCriteria.copy();

        assertThat(petTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(petTypeCriteria)
        );
    }

    @Test
    void petTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var petTypeCriteria = new PetTypeCriteria();
        setAllFilters(petTypeCriteria);

        var copy = petTypeCriteria.copy();

        assertThat(petTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(petTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var petTypeCriteria = new PetTypeCriteria();

        assertThat(petTypeCriteria).hasToString("PetTypeCriteria{}");
    }

    private static void setAllFilters(PetTypeCriteria petTypeCriteria) {
        petTypeCriteria.id();
        petTypeCriteria.name();
        petTypeCriteria.distinct();
    }

    private static Condition<PetTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PetTypeCriteria> copyFiltersAre(PetTypeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
