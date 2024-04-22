package org.springframework.samples.petclinic.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link org.springframework.samples.petclinic.domain.Speciality} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<VetDTO> vets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<VetDTO> getVets() {
        return vets;
    }

    public void setVets(Set<VetDTO> vets) {
        this.vets = vets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialityDTO)) {
            return false;
        }

        SpecialityDTO specialityDTO = (SpecialityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specialityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialityDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", vets=" + getVets() +
            "}";
    }
}
