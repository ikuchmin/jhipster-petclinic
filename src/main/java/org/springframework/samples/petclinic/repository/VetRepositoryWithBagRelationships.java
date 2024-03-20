package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.samples.petclinic.domain.Vet;

public interface VetRepositoryWithBagRelationships {
    Optional<Vet> fetchBagRelationships(Optional<Vet> vet);

    List<Vet> fetchBagRelationships(List<Vet> vets);

    Page<Vet> fetchBagRelationships(Page<Vet> vets);
}
