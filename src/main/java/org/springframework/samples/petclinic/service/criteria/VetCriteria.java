package org.springframework.samples.petclinic.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.springframework.samples.petclinic.domain.Vet} entity. This class is used
 * in {@link org.springframework.samples.petclinic.web.rest.VetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private BigDecimalFilter salary;

    private LongFilter visitsId;

    private LongFilter specialitiesId;

    private Boolean distinct;

    public VetCriteria() {}

    public VetCriteria(VetCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.salary = other.optionalSalary().map(BigDecimalFilter::copy).orElse(null);
        this.visitsId = other.optionalVisitsId().map(LongFilter::copy).orElse(null);
        this.specialitiesId = other.optionalSpecialitiesId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VetCriteria copy() {
        return new VetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public BigDecimalFilter getSalary() {
        return salary;
    }

    public Optional<BigDecimalFilter> optionalSalary() {
        return Optional.ofNullable(salary);
    }

    public BigDecimalFilter salary() {
        if (salary == null) {
            setSalary(new BigDecimalFilter());
        }
        return salary;
    }

    public void setSalary(BigDecimalFilter salary) {
        this.salary = salary;
    }

    public LongFilter getVisitsId() {
        return visitsId;
    }

    public Optional<LongFilter> optionalVisitsId() {
        return Optional.ofNullable(visitsId);
    }

    public LongFilter visitsId() {
        if (visitsId == null) {
            setVisitsId(new LongFilter());
        }
        return visitsId;
    }

    public void setVisitsId(LongFilter visitsId) {
        this.visitsId = visitsId;
    }

    public LongFilter getSpecialitiesId() {
        return specialitiesId;
    }

    public Optional<LongFilter> optionalSpecialitiesId() {
        return Optional.ofNullable(specialitiesId);
    }

    public LongFilter specialitiesId() {
        if (specialitiesId == null) {
            setSpecialitiesId(new LongFilter());
        }
        return specialitiesId;
    }

    public void setSpecialitiesId(LongFilter specialitiesId) {
        this.specialitiesId = specialitiesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VetCriteria that = (VetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(visitsId, that.visitsId) &&
            Objects.equals(specialitiesId, that.specialitiesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, salary, visitsId, specialitiesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VetCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalSalary().map(f -> "salary=" + f + ", ").orElse("") +
            optionalVisitsId().map(f -> "visitsId=" + f + ", ").orElse("") +
            optionalSpecialitiesId().map(f -> "specialitiesId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
