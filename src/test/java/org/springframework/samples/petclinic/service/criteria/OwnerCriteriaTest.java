package org.springframework.samples.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OwnerCriteriaTest {

    @Test
    void newOwnerCriteriaHasAllFiltersNullTest() {
        var ownerCriteria = new OwnerCriteria();
        assertThat(ownerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void ownerCriteriaFluentMethodsCreatesFiltersTest() {
        var ownerCriteria = new OwnerCriteria();

        setAllFilters(ownerCriteria);

        assertThat(ownerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void ownerCriteriaCopyCreatesNullFilterTest() {
        var ownerCriteria = new OwnerCriteria();
        var copy = ownerCriteria.copy();

        assertThat(ownerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(ownerCriteria)
        );
    }

    @Test
    void ownerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ownerCriteria = new OwnerCriteria();
        setAllFilters(ownerCriteria);

        var copy = ownerCriteria.copy();

        assertThat(ownerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(ownerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ownerCriteria = new OwnerCriteria();

        assertThat(ownerCriteria).hasToString("OwnerCriteria{}");
    }

    private static void setAllFilters(OwnerCriteria ownerCriteria) {
        ownerCriteria.id();
        ownerCriteria.firstName();
        ownerCriteria.lastName();
        ownerCriteria.email();
        ownerCriteria.address();
        ownerCriteria.city();
        ownerCriteria.telephone();
        ownerCriteria.petsId();
        ownerCriteria.distinct();
    }

    private static Condition<OwnerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getPetsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OwnerCriteria> copyFiltersAre(OwnerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getPetsId(), copy.getPetsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
