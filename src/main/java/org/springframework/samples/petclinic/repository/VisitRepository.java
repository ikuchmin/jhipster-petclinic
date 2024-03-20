package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.samples.petclinic.domain.Visit;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {}
