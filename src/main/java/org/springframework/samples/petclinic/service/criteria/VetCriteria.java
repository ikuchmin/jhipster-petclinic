package org.springframework.samples.petclinic.service.criteria;

import java.io.Serializable;
import java.util.Objects;
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
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.visitsId = other.visitsId == null ? null : other.visitsId.copy();
        this.specialitiesId = other.specialitiesId == null ? null : other.specialitiesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VetCriteria copy() {
        return new VetCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public BigDecimalFilter getSalary() {
        return salary;
    }

    public BigDecimalFilter salary() {
        if (salary == null) {
            salary = new BigDecimalFilter();
        }
        return salary;
    }

    public void setSalary(BigDecimalFilter salary) {
        this.salary = salary;
    }

    public LongFilter getVisitsId() {
        return visitsId;
    }

    public LongFilter visitsId() {
        if (visitsId == null) {
            visitsId = new LongFilter();
        }
        return visitsId;
    }

    public void setVisitsId(LongFilter visitsId) {
        this.visitsId = visitsId;
    }

    public LongFilter getSpecialitiesId() {
        return specialitiesId;
    }

    public LongFilter specialitiesId() {
        if (specialitiesId == null) {
            specialitiesId = new LongFilter();
        }
        return specialitiesId;
    }

    public void setSpecialitiesId(LongFilter specialitiesId) {
        this.specialitiesId = specialitiesId;
    }

    public Boolean getDistinct() {
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
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (salary != null ? "salary=" + salary + ", " : "") +
            (visitsId != null ? "visitsId=" + visitsId + ", " : "") +
            (specialitiesId != null ? "specialitiesId=" + specialitiesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
