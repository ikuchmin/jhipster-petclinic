package org.springframework.samples.petclinic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vet.
 */
@Entity
@Table(name = "vet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "salary", precision = 21, scale = 2, nullable = false)
    private BigDecimal salary;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vet", "pet" }, allowSetters = true)
    private Set<Visit> visits = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_vet__specialities",
        joinColumns = @JoinColumn(name = "vet_id"),
        inverseJoinColumns = @JoinColumn(name = "specialities_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vets" }, allowSetters = true)
    private Set<Speciality> specialities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Vet firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Vet lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public Vet salary(BigDecimal salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Set<Visit> getVisits() {
        return this.visits;
    }

    public void setVisits(Set<Visit> visits) {
        if (this.visits != null) {
            this.visits.forEach(i -> i.setVet(null));
        }
        if (visits != null) {
            visits.forEach(i -> i.setVet(this));
        }
        this.visits = visits;
    }

    public Vet visits(Set<Visit> visits) {
        this.setVisits(visits);
        return this;
    }

    public Vet addVisits(Visit visit) {
        this.visits.add(visit);
        visit.setVet(this);
        return this;
    }

    public Vet removeVisits(Visit visit) {
        this.visits.remove(visit);
        visit.setVet(null);
        return this;
    }

    public Set<Speciality> getSpecialities() {
        return this.specialities;
    }

    public void setSpecialities(Set<Speciality> specialities) {
        this.specialities = specialities;
    }

    public Vet specialities(Set<Speciality> specialities) {
        this.setSpecialities(specialities);
        return this;
    }

    public Vet addSpecialities(Speciality speciality) {
        this.specialities.add(speciality);
        return this;
    }

    public Vet removeSpecialities(Speciality speciality) {
        this.specialities.remove(speciality);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vet)) {
            return false;
        }
        return getId() != null && getId().equals(((Vet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vet{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
