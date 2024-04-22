package org.springframework.samples.petclinic.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.service.dto.OwnerDTO;
import org.springframework.samples.petclinic.service.mapper.OwnerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.springframework.samples.petclinic.domain.Owner}.
 */
@Service
@Transactional
public class OwnerService {

    private final Logger log = LoggerFactory.getLogger(OwnerService.class);

    private final OwnerRepository ownerRepository;

    private final OwnerMapper ownerMapper;

    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    /**
     * Save a owner.
     *
     * @param ownerDTO the entity to save.
     * @return the persisted entity.
     */
    public OwnerDTO save(OwnerDTO ownerDTO) {
        log.debug("Request to save Owner : {}", ownerDTO);
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner = ownerRepository.save(owner);
        return ownerMapper.toDto(owner);
    }

    /**
     * Update a owner.
     *
     * @param ownerDTO the entity to save.
     * @return the persisted entity.
     */
    public OwnerDTO update(OwnerDTO ownerDTO) {
        log.debug("Request to update Owner : {}", ownerDTO);
        Owner owner = ownerMapper.toEntity(ownerDTO);
        owner = ownerRepository.save(owner);
        return ownerMapper.toDto(owner);
    }

    /**
     * Partially update a owner.
     *
     * @param ownerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OwnerDTO> partialUpdate(OwnerDTO ownerDTO) {
        log.debug("Request to partially update Owner : {}", ownerDTO);

        return ownerRepository
            .findById(ownerDTO.getId())
            .map(existingOwner -> {
                ownerMapper.partialUpdate(existingOwner, ownerDTO);

                return existingOwner;
            })
            .map(ownerRepository::save)
            .map(ownerMapper::toDto);
    }

    /**
     * Get one owner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OwnerDTO> findOne(Long id) {
        log.debug("Request to get Owner : {}", id);
        return ownerRepository.findById(id).map(ownerMapper::toDto);
    }

    /**
     * Delete the owner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
    }
}
