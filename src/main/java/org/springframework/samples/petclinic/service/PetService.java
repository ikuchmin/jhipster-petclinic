package org.springframework.samples.petclinic.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.domain.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.dto.PetDTO;
import org.springframework.samples.petclinic.service.mapper.PetMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.springframework.samples.petclinic.domain.Pet}.
 */
@Service
@Transactional
public class PetService {

    private final Logger log = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;

    private final PetMapper petMapper;

    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    /**
     * Save a pet.
     *
     * @param petDTO the entity to save.
     * @return the persisted entity.
     */
    public PetDTO save(PetDTO petDTO) {
        log.debug("Request to save Pet : {}", petDTO);
        Pet pet = petMapper.toEntity(petDTO);
        pet = petRepository.save(pet);
        return petMapper.toDto(pet);
    }

    /**
     * Update a pet.
     *
     * @param petDTO the entity to save.
     * @return the persisted entity.
     */
    public PetDTO update(PetDTO petDTO) {
        log.debug("Request to update Pet : {}", petDTO);
        Pet pet = petMapper.toEntity(petDTO);
        pet = petRepository.save(pet);
        return petMapper.toDto(pet);
    }

    /**
     * Partially update a pet.
     *
     * @param petDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PetDTO> partialUpdate(PetDTO petDTO) {
        log.debug("Request to partially update Pet : {}", petDTO);

        return petRepository
            .findById(petDTO.getId())
            .map(existingPet -> {
                petMapper.partialUpdate(existingPet, petDTO);

                return existingPet;
            })
            .map(petRepository::save)
            .map(petMapper::toDto);
    }

    /**
     * Get one pet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PetDTO> findOne(Long id) {
        log.debug("Request to get Pet : {}", id);
        return petRepository.findById(id).map(petMapper::toDto);
    }

    /**
     * Delete the pet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pet : {}", id);
        petRepository.deleteById(id);
    }
}
