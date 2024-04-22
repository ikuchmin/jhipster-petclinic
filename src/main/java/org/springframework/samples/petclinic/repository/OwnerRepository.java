package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Owner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long>, JpaSpecificationExecutor<Owner> {}
