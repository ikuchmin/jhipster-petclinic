package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Speciality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long>, JpaSpecificationExecutor<Speciality> {}
