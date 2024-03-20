package org.springframework.samples.petclinic.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.springframework.samples.petclinic.domain.Visit} entity. This class is used
 * in {@link org.springframework.samples.petclinic.web.rest.VisitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /visits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private LongFilter vetId;

    private LongFilter petId;

    private Boolean distinct;

    public VisitCriteria() {}

    public VisitCriteria(VisitCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.vetId = other.vetId == null ? null : other.vetId.copy();
        this.petId = other.petId == null ? null : other.petId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VisitCriteria copy() {
        return new VisitCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getVetId() {
        return vetId;
    }

    public LongFilter vetId() {
        if (vetId == null) {
            vetId = new LongFilter();
        }
        return vetId;
    }

    public void setVetId(LongFilter vetId) {
        this.vetId = vetId;
    }

    public LongFilter getPetId() {
        return petId;
    }

    public LongFilter petId() {
        if (petId == null) {
            petId = new LongFilter();
        }
        return petId;
    }

    public void setPetId(LongFilter petId) {
        this.petId = petId;
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
        final VisitCriteria that = (VisitCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(vetId, that.vetId) &&
            Objects.equals(petId, that.petId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, vetId, petId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (vetId != null ? "vetId=" + vetId + ", " : "") +
            (petId != null ? "petId=" + petId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
