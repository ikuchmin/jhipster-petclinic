package org.springframework.samples.petclinic.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
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
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.visitsId = other.optionalVisitsId().map(LongFilter::copy).orElse(null);
        this.typeId = other.optionalTypeId().map(LongFilter::copy).orElse(null);
        this.ownerId = other.optionalOwnerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PetCriteria copy() {
        return new PetCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
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

    public LongFilter getTypeId() {
        return typeId;
    }

    public Optional<LongFilter> optionalTypeId() {
        return Optional.ofNullable(typeId);
    }

    public LongFilter typeId() {
        if (typeId == null) {
            setTypeId(new LongFilter());
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public Optional<LongFilter> optionalOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            setOwnerId(new LongFilter());
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
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
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalVisitsId().map(f -> "visitsId=" + f + ", ").orElse("") +
            optionalTypeId().map(f -> "typeId=" + f + ", ").orElse("") +
            optionalOwnerId().map(f -> "ownerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
