package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.samples.petclinic.domain.Pet;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {}
