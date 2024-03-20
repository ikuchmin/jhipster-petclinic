package org.springframework.samples.petclinic.web.rest;

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
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.VisitQueryService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.service.criteria.VisitCriteria;
import org.springframework.samples.petclinic.service.dto.VisitDTO;
import org.springframework.samples.petclinic.web.rest.errors.BadRequestAlertException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.springframework.samples.petclinic.domain.Visit}.
 */
@RestController
@RequestMapping("/api/visits")
public class VisitResource {

    private final Logger log = LoggerFactory.getLogger(VisitResource.class);

    private static final String ENTITY_NAME = "visit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitService visitService;

    private final VisitRepository visitRepository;

    private final VisitQueryService visitQueryService;

    public VisitResource(VisitService visitService, VisitRepository visitRepository, VisitQueryService visitQueryService) {
        this.visitService = visitService;
        this.visitRepository = visitRepository;
        this.visitQueryService = visitQueryService;
    }

    /**
     * {@code POST  /visits} : Create a new visit.
     *
     * @param visitDTO the visitDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visitDTO, or with status {@code 400 (Bad Request)} if the visit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VisitDTO> createVisit(@RequestBody VisitDTO visitDTO) throws URISyntaxException {
        log.debug("REST request to save Visit : {}", visitDTO);
        if (visitDTO.getId() != null) {
            throw new BadRequestAlertException("A new visit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisitDTO result = visitService.save(visitDTO);
        return ResponseEntity
            .created(new URI("/api/visits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visits/:id} : Updates an existing visit.
     *
     * @param id the id of the visitDTO to save.
     * @param visitDTO the visitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitDTO,
     * or with status {@code 400 (Bad Request)} if the visitDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VisitDTO> updateVisit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VisitDTO visitDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Visit : {}, {}", id, visitDTO);
        if (visitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VisitDTO result = visitService.update(visitDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /visits/:id} : Partial updates given fields of an existing visit, field will ignore if it is null
     *
     * @param id the id of the visitDTO to save.
     * @param visitDTO the visitDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitDTO,
     * or with status {@code 400 (Bad Request)} if the visitDTO is not valid,
     * or with status {@code 404 (Not Found)} if the visitDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the visitDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VisitDTO> partialUpdateVisit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VisitDTO visitDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Visit partially : {}, {}", id, visitDTO);
        if (visitDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VisitDTO> result = visitService.partialUpdate(visitDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /visits} : get all the visits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VisitDTO>> getAllVisits(
        VisitCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Visits by criteria: {}", criteria);

        Page<VisitDTO> page = visitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /visits/count} : count all the visits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVisits(VisitCriteria criteria) {
        log.debug("REST request to count Visits by criteria: {}", criteria);
        return ResponseEntity.ok().body(visitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /visits/:id} : get the "id" visit.
     *
     * @param id the id of the visitDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visitDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisit(@PathVariable("id") Long id) {
        log.debug("REST request to get Visit : {}", id);
        Optional<VisitDTO> visitDTO = visitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(visitDTO);
    }

    /**
     * {@code DELETE  /visits/:id} : delete the "id" visit.
     *
     * @param id the id of the visitDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable("id") Long id) {
        log.debug("REST request to delete Visit : {}", id);
        visitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
