package org.springframework.samples.petclinic.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link org.springframework.samples.petclinic.domain.Vet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VetDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private BigDecimal salary;

    private Set<SpecialityDTO> specialities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Set<SpecialityDTO> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(Set<SpecialityDTO> specialities) {
        this.specialities = specialities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VetDTO)) {
            return false;
        }

        VetDTO vetDTO = (VetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VetDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", salary=" + getSalary() +
            ", specialities=" + getSpecialities() +
            "}";
    }
}
