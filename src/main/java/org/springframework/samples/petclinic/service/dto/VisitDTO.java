package org.springframework.samples.petclinic.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.springframework.samples.petclinic.domain.Visit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private VetDTO vet;

    private PetDTO pet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public VetDTO getVet() {
        return vet;
    }

    public void setVet(VetDTO vet) {
        this.vet = vet;
    }

    public PetDTO getPet() {
        return pet;
    }

    public void setPet(PetDTO pet) {
        this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisitDTO)) {
            return false;
        }

        VisitDTO visitDTO = (VisitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, visitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", vet=" + getVet() +
            ", pet=" + getPet() +
            "}";
    }
}
