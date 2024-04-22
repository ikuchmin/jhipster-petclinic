package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.samples.petclinic.domain.Authority;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
