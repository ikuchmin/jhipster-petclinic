package org.springframework.samples.petclinic.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.samples.petclinic.domain.*; // for static metamodels
import org.springframework.samples.petclinic.domain.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.service.criteria.PetTypeCriteria;
import org.springframework.samples.petclinic.service.dto.PetTypeDTO;
import org.springframework.samples.petclinic.service.mapper.PetTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PetType} entities in the database.
 * The main input is a {@link PetTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PetTypeDTO} or a {@link Page} of {@link PetTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PetTypeQueryService extends QueryService<PetType> {

    private final Logger log = LoggerFactory.getLogger(PetTypeQueryService.class);

    private final PetTypeRepository petTypeRepository;

    private final PetTypeMapper petTypeMapper;

    public PetTypeQueryService(PetTypeRepository petTypeRepository, PetTypeMapper petTypeMapper) {
        this.petTypeRepository = petTypeRepository;
        this.petTypeMapper = petTypeMapper;
    }

    /**
     * Return a {@link List} of {@link PetTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PetTypeDTO> findByCriteria(PetTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PetType> specification = createSpecification(criteria);
        return petTypeMapper.toDto(petTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PetTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PetTypeDTO> findByCriteria(PetTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PetType> specification = createSpecification(criteria);
        return petTypeRepository.findAll(specification, page).map(petTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PetTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PetType> specification = createSpecification(criteria);
        return petTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PetTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PetType> createSpecification(PetTypeCriteria criteria) {
        Specification<PetType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PetType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PetType_.name));
            }
        }
        return specification;
    }
}
