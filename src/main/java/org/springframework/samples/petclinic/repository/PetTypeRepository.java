package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.samples.petclinic.domain.PetType;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PetType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long>, JpaSpecificationExecutor<PetType> {}
