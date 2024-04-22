package org.springframework.samples.petclinic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Speciality.
 */
@Entity
@Table(name = "speciality")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Speciality implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "specialities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visits", "specialities" }, allowSetters = true)
    private Set<Vet> vets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Speciality id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Speciality name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Vet> getVets() {
        return this.vets;
    }

    public void setVets(Set<Vet> vets) {
        if (this.vets != null) {
            this.vets.forEach(i -> i.removeSpecialities(this));
        }
        if (vets != null) {
            vets.forEach(i -> i.addSpecialities(this));
        }
        this.vets = vets;
    }

    public Speciality vets(Set<Vet> vets) {
        this.setVets(vets);
        return this;
    }

    public Speciality addVet(Vet vet) {
        this.vets.add(vet);
        vet.getSpecialities().add(this);
        return this;
    }

    public Speciality removeVet(Vet vet) {
        this.vets.remove(vet);
        vet.getSpecialities().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Speciality)) {
            return false;
        }
        return getId() != null && getId().equals(((Speciality) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Speciality{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
