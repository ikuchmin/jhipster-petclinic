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
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.VetQueryService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.criteria.VetCriteria;
import org.springframework.samples.petclinic.service.dto.VetDTO;
import org.springframework.samples.petclinic.web.rest.errors.BadRequestAlertException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.springframework.samples.petclinic.domain.Vet}.
 */
@RestController
@RequestMapping("/api/vets")
public class VetResource {

    private final Logger log = LoggerFactory.getLogger(VetResource.class);

    private static final String ENTITY_NAME = "vet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VetService vetService;

    private final VetRepository vetRepository;

    private final VetQueryService vetQueryService;

    public VetResource(VetService vetService, VetRepository vetRepository, VetQueryService vetQueryService) {
        this.vetService = vetService;
        this.vetRepository = vetRepository;
        this.vetQueryService = vetQueryService;
    }

    /**
     * {@code POST  /vets} : Create a new vet.
     *
     * @param vetDTO the vetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vetDTO, or with status {@code 400 (Bad Request)} if the vet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VetDTO> createVet(@Valid @RequestBody VetDTO vetDTO) throws URISyntaxException {
        log.debug("REST request to save Vet : {}", vetDTO);
        if (vetDTO.getId() != null) {
            throw new BadRequestAlertException("A new vet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vetDTO = vetService.save(vetDTO);
        return ResponseEntity.created(new URI("/api/vets/" + vetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vetDTO.getId().toString()))
            .body(vetDTO);
    }

    /**
     * {@code PUT  /vets/:id} : Updates an existing vet.
     *
     * @param id the id of the vetDTO to save.
     * @param vetDTO the vetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vetDTO,
     * or with status {@code 400 (Bad Request)} if the vetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VetDTO> updateVet(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody VetDTO vetDTO)
        throws URISyntaxException {
        log.debug("REST request to update Vet : {}, {}", id, vetDTO);
        if (vetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vetDTO = vetService.update(vetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vetDTO.getId().toString()))
            .body(vetDTO);
    }

    /**
     * {@code PATCH  /vets/:id} : Partial updates given fields of an existing vet, field will ignore if it is null
     *
     * @param id the id of the vetDTO to save.
     * @param vetDTO the vetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vetDTO,
     * or with status {@code 400 (Bad Request)} if the vetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VetDTO> partialUpdateVet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VetDTO vetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vet partially : {}, {}", id, vetDTO);
        if (vetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VetDTO> result = vetService.partialUpdate(vetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vets} : get all the vets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VetDTO>> getAllVets(
        VetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Vets by criteria: {}", criteria);

        Page<VetDTO> page = vetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vets/count} : count all the vets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVets(VetCriteria criteria) {
        log.debug("REST request to count Vets by criteria: {}", criteria);
        return ResponseEntity.ok().body(vetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vets/:id} : get the "id" vet.
     *
     * @param id the id of the vetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VetDTO> getVet(@PathVariable("id") Long id) {
        log.debug("REST request to get Vet : {}", id);
        Optional<VetDTO> vetDTO = vetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vetDTO);
    }

    /**
     * {@code DELETE  /vets/:id} : delete the "id" vet.
     *
     * @param id the id of the vetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable("id") Long id) {
        log.debug("REST request to delete Vet : {}", id);
        vetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
