package org.springframework.samples.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SpecialityCriteriaTest {

    @Test
    void newSpecialityCriteriaHasAllFiltersNullTest() {
        var specialityCriteria = new SpecialityCriteria();
        assertThat(specialityCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void specialityCriteriaFluentMethodsCreatesFiltersTest() {
        var specialityCriteria = new SpecialityCriteria();

        setAllFilters(specialityCriteria);

        assertThat(specialityCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void specialityCriteriaCopyCreatesNullFilterTest() {
        var specialityCriteria = new SpecialityCriteria();
        var copy = specialityCriteria.copy();

        assertThat(specialityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(specialityCriteria)
        );
    }

    @Test
    void specialityCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var specialityCriteria = new SpecialityCriteria();
        setAllFilters(specialityCriteria);

        var copy = specialityCriteria.copy();

        assertThat(specialityCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(specialityCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var specialityCriteria = new SpecialityCriteria();

        assertThat(specialityCriteria).hasToString("SpecialityCriteria{}");
    }

    private static void setAllFilters(SpecialityCriteria specialityCriteria) {
        specialityCriteria.id();
        specialityCriteria.name();
        specialityCriteria.vetId();
        specialityCriteria.distinct();
    }

    private static Condition<SpecialityCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getVetId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SpecialityCriteria> copyFiltersAre(SpecialityCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getVetId(), copy.getVetId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
