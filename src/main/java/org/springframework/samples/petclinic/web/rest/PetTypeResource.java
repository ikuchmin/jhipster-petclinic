package org.springframework.samples.petclinic.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.service.PetTypeQueryService;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.samples.petclinic.service.criteria.PetTypeCriteria;
import org.springframework.samples.petclinic.service.dto.PetTypeDTO;
import org.springframework.samples.petclinic.web.rest.errors.BadRequestAlertException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.springframework.samples.petclinic.domain.PetType}.
 */
@RestController
@RequestMapping("/api/pet-types")
public class PetTypeResource {

    private final Logger log = LoggerFactory.getLogger(PetTypeResource.class);

    private static final String ENTITY_NAME = "petType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PetTypeService petTypeService;

    private final PetTypeRepository petTypeRepository;

    private final PetTypeQueryService petTypeQueryService;

    public PetTypeResource(PetTypeService petTypeService, PetTypeRepository petTypeRepository, PetTypeQueryService petTypeQueryService) {
        this.petTypeService = petTypeService;
        this.petTypeRepository = petTypeRepository;
        this.petTypeQueryService = petTypeQueryService;
    }

    /**
     * {@code POST  /pet-types} : Create a new petType.
     *
     * @param petTypeDTO the petTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new petTypeDTO, or with status {@code 400 (Bad Request)} if the petType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PetTypeDTO> createPetType(@Valid @RequestBody PetTypeDTO petTypeDTO) throws URISyntaxException {
        log.debug("REST request to save PetType : {}", petTypeDTO);
        if (petTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new petType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PetTypeDTO result = petTypeService.save(petTypeDTO);
        return ResponseEntity
            .created(new URI("/api/pet-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pet-types/:id} : Updates an existing petType.
     *
     * @param id the id of the petTypeDTO to save.
     * @param petTypeDTO the petTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petTypeDTO,
     * or with status {@code 400 (Bad Request)} if the petTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the petTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PetTypeDTO> updatePetType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PetTypeDTO petTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PetType : {}, {}", id, petTypeDTO);
        if (petTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PetTypeDTO result = petTypeService.update(petTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pet-types/:id} : Partial updates given fields of an existing petType, field will ignore if it is null
     *
     * @param id the id of the petTypeDTO to save.
     * @param petTypeDTO the petTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petTypeDTO,
     * or with status {@code 400 (Bad Request)} if the petTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the petTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the petTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PetTypeDTO> partialUpdatePetType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PetTypeDTO petTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PetType partially : {}, {}", id, petTypeDTO);
        if (petTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PetTypeDTO> result = petTypeService.partialUpdate(petTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pet-types} : get all the petTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of petTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PetTypeDTO>> getAllPetTypes(
        PetTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PetTypes by criteria: {}", criteria);

        Page<PetTypeDTO> page = petTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pet-types/count} : count all the petTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPetTypes(PetTypeCriteria criteria) {
        log.debug("REST request to count PetTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(petTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pet-types/:id} : get the "id" petType.
     *
     * @param id the id of the petTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the petTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetTypeDTO> getPetType(@PathVariable("id") Long id) {
        log.debug("REST request to get PetType : {}", id);
        Optional<PetTypeDTO> petTypeDTO = petTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(petTypeDTO);
    }

    /**
     * {@code DELETE  /pet-types/:id} : delete the "id" petType.
     *
     * @param id the id of the petTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetType(@PathVariable("id") Long id) {
        log.debug("REST request to delete PetType : {}", id);
        petTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
