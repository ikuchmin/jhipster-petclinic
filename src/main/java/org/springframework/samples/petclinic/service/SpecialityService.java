package org.springframework.samples.petclinic.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.samples.petclinic.repository.SpecialityRepository;
import org.springframework.samples.petclinic.service.dto.SpecialityDTO;
import org.springframework.samples.petclinic.service.mapper.SpecialityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.springframework.samples.petclinic.domain.Speciality}.
 */
@Service
@Transactional
public class SpecialityService {

    private final Logger log = LoggerFactory.getLogger(SpecialityService.class);

    private final SpecialityRepository specialityRepository;

    private final SpecialityMapper specialityMapper;

    public SpecialityService(SpecialityRepository specialityRepository, SpecialityMapper specialityMapper) {
        this.specialityRepository = specialityRepository;
        this.specialityMapper = specialityMapper;
    }

    /**
     * Save a speciality.
     *
     * @param specialityDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialityDTO save(SpecialityDTO specialityDTO) {
        log.debug("Request to save Speciality : {}", specialityDTO);
        Speciality speciality = specialityMapper.toEntity(specialityDTO);
        speciality = specialityRepository.save(speciality);
        return specialityMapper.toDto(speciality);
    }

    /**
     * Update a speciality.
     *
     * @param specialityDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialityDTO update(SpecialityDTO specialityDTO) {
        log.debug("Request to update Speciality : {}", specialityDTO);
        Speciality speciality = specialityMapper.toEntity(specialityDTO);
        speciality = specialityRepository.save(speciality);
        return specialityMapper.toDto(speciality);
    }

    /**
     * Partially update a speciality.
     *
     * @param specialityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpecialityDTO> partialUpdate(SpecialityDTO specialityDTO) {
        log.debug("Request to partially update Speciality : {}", specialityDTO);

        return specialityRepository
            .findById(specialityDTO.getId())
            .map(existingSpeciality -> {
                specialityMapper.partialUpdate(existingSpeciality, specialityDTO);

                return existingSpeciality;
            })
            .map(specialityRepository::save)
            .map(specialityMapper::toDto);
    }

    /**
     * Get all the specialities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecialityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Specialities");
        return specialityRepository.findAll(pageable).map(specialityMapper::toDto);
    }

    /**
     * Get one speciality by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecialityDTO> findOne(Long id) {
        log.debug("Request to get Speciality : {}", id);
        return specialityRepository.findById(id).map(specialityMapper::toDto);
    }

    /**
     * Delete the speciality by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Speciality : {}", id);
        specialityRepository.deleteById(id);
    }
}
