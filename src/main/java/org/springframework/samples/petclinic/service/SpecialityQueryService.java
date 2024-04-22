package org.springframework.samples.petclinic.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.samples.petclinic.domain.*; // for static metamodels
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.samples.petclinic.repository.SpecialityRepository;
import org.springframework.samples.petclinic.service.criteria.SpecialityCriteria;
import org.springframework.samples.petclinic.service.dto.SpecialityDTO;
import org.springframework.samples.petclinic.service.mapper.SpecialityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Speciality} entities in the database.
 * The main input is a {@link SpecialityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SpecialityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecialityQueryService extends QueryService<Speciality> {

    private final Logger log = LoggerFactory.getLogger(SpecialityQueryService.class);

    private final SpecialityRepository specialityRepository;

    private final SpecialityMapper specialityMapper;

    public SpecialityQueryService(SpecialityRepository specialityRepository, SpecialityMapper specialityMapper) {
        this.specialityRepository = specialityRepository;
        this.specialityMapper = specialityMapper;
    }

    /**
     * Return a {@link Page} of {@link SpecialityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecialityDTO> findByCriteria(SpecialityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Speciality> specification = createSpecification(criteria);
        return specialityRepository.findAll(specification, page).map(specialityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecialityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Speciality> specification = createSpecification(criteria);
        return specialityRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecialityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Speciality> createSpecification(SpecialityCriteria criteria) {
        Specification<Speciality> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Speciality_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Speciality_.name));
            }
            if (criteria.getVetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVetId(), root -> root.join(Speciality_.vets, JoinType.LEFT).get(Vet_.id))
                );
            }
        }
        return specification;
    }
}
