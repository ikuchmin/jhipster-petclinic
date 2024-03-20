package org.springframework.samples.petclinic.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.samples.petclinic.domain.*; // for static metamodels
import org.springframework.samples.petclinic.domain.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.criteria.VetCriteria;
import org.springframework.samples.petclinic.service.dto.VetDTO;
import org.springframework.samples.petclinic.service.mapper.VetMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Vet} entities in the database.
 * The main input is a {@link VetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VetDTO} or a {@link Page} of {@link VetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VetQueryService extends QueryService<Vet> {

    private final Logger log = LoggerFactory.getLogger(VetQueryService.class);

    private final VetRepository vetRepository;

    private final VetMapper vetMapper;

    public VetQueryService(VetRepository vetRepository, VetMapper vetMapper) {
        this.vetRepository = vetRepository;
        this.vetMapper = vetMapper;
    }

    /**
     * Return a {@link List} of {@link VetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VetDTO> findByCriteria(VetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vet> specification = createSpecification(criteria);
        return vetMapper.toDto(vetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VetDTO> findByCriteria(VetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vet> specification = createSpecification(criteria);
        return vetRepository.findAll(specification, page).map(vetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vet> specification = createSpecification(criteria);
        return vetRepository.count(specification);
    }

    /**
     * Function to convert {@link VetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vet> createSpecification(VetCriteria criteria) {
        Specification<Vet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vet_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Vet_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Vet_.lastName));
            }
            if (criteria.getVisitsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVisitsId(), root -> root.join(Vet_.visits, JoinType.LEFT).get(Visit_.id))
                    );
            }
            if (criteria.getSpecialitiesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSpecialitiesId(),
                            root -> root.join(Vet_.specialities, JoinType.LEFT).get(Speciality_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
