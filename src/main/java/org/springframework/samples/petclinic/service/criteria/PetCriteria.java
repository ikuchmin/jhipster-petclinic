package org.springframework.samples.petclinic.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.springframework.samples.petclinic.domain.Pet} entity. This class is used
 * in {@link org.springframework.samples.petclinic.web.rest.PetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter birthDate;

    private LongFilter visitsId;

    private LongFilter typeId;

    private LongFilter ownerId;

    private Boolean distinct;

    public PetCriteria() {}

    public PetCriteria(PetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.visitsId = other.visitsId == null ? null : other.visitsId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PetCriteria copy() {
        return new PetCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            birthDate = new LocalDateFilter();
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
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

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
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
        final PetCriteria that = (PetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(visitsId, that.visitsId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthDate, visitsId, typeId, ownerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (visitsId != null ? "visitsId=" + visitsId + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
