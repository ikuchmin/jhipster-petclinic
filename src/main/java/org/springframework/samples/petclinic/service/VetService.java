package org.springframework.samples.petclinic.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.dto.VetDTO;
import org.springframework.samples.petclinic.service.mapper.VetMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.springframework.samples.petclinic.domain.Vet}.
 */
@Service
@Transactional
public class VetService {

    private final Logger log = LoggerFactory.getLogger(VetService.class);

    private final VetRepository vetRepository;

    private final VetMapper vetMapper;

    public VetService(VetRepository vetRepository, VetMapper vetMapper) {
        this.vetRepository = vetRepository;
        this.vetMapper = vetMapper;
    }

    /**
     * Save a vet.
     *
     * @param vetDTO the entity to save.
     * @return the persisted entity.
     */
    public VetDTO save(VetDTO vetDTO) {
        log.debug("Request to save Vet : {}", vetDTO);
        Vet vet = vetMapper.toEntity(vetDTO);
        vet = vetRepository.save(vet);
        return vetMapper.toDto(vet);
    }

    /**
     * Update a vet.
     *
     * @param vetDTO the entity to save.
     * @return the persisted entity.
     */
    public VetDTO update(VetDTO vetDTO) {
        log.debug("Request to update Vet : {}", vetDTO);
        Vet vet = vetMapper.toEntity(vetDTO);
        vet = vetRepository.save(vet);
        return vetMapper.toDto(vet);
    }

    /**
     * Partially update a vet.
     *
     * @param vetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VetDTO> partialUpdate(VetDTO vetDTO) {
        log.debug("Request to partially update Vet : {}", vetDTO);

        return vetRepository
            .findById(vetDTO.getId())
            .map(existingVet -> {
                vetMapper.partialUpdate(existingVet, vetDTO);

                return existingVet;
            })
            .map(vetRepository::save)
            .map(vetMapper::toDto);
    }

    /**
     * Get all the vets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vets");
        return vetRepository.findAll(pageable).map(vetMapper::toDto);
    }

    /**
     * Get all the vets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VetDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vetRepository.findAllWithEagerRelationships(pageable).map(vetMapper::toDto);
    }

    /**
     * Get one vet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VetDTO> findOne(Long id) {
        log.debug("Request to get Vet : {}", id);
        return vetRepository.findOneWithEagerRelationships(id).map(vetMapper::toDto);
    }

    /**
     * Delete the vet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vet : {}", id);
        vetRepository.deleteById(id);
    }
}
