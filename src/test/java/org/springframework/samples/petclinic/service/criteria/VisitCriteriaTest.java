package org.springframework.samples.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VisitCriteriaTest {

    @Test
    void newVisitCriteriaHasAllFiltersNullTest() {
        var visitCriteria = new VisitCriteria();
        assertThat(visitCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void visitCriteriaFluentMethodsCreatesFiltersTest() {
        var visitCriteria = new VisitCriteria();

        setAllFilters(visitCriteria);

        assertThat(visitCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void visitCriteriaCopyCreatesNullFilterTest() {
        var visitCriteria = new VisitCriteria();
        var copy = visitCriteria.copy();

        assertThat(visitCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(visitCriteria)
        );
    }

    @Test
    void visitCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var visitCriteria = new VisitCriteria();
        setAllFilters(visitCriteria);

        var copy = visitCriteria.copy();

        assertThat(visitCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(visitCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var visitCriteria = new VisitCriteria();

        assertThat(visitCriteria).hasToString("VisitCriteria{}");
    }

    private static void setAllFilters(VisitCriteria visitCriteria) {
        visitCriteria.id();
        visitCriteria.date();
        visitCriteria.vetId();
        visitCriteria.petId();
        visitCriteria.distinct();
    }

    private static Condition<VisitCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getVetId()) &&
                condition.apply(criteria.getPetId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VisitCriteria> copyFiltersAre(VisitCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getVetId(), copy.getVetId()) &&
                condition.apply(criteria.getPetId(), copy.getPetId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
