package org.springframework.samples.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.IntegrationTest;
import org.springframework.samples.petclinic.domain.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.service.dto.PetTypeDTO;
import org.springframework.samples.petclinic.service.mapper.PetTypeMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PetTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PetTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pet-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetTypeMockMvc;

    private PetType petType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PetType createEntity(EntityManager em) {
        PetType petType = new PetType().name(DEFAULT_NAME);
        return petType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PetType createUpdatedEntity(EntityManager em) {
        PetType petType = new PetType().name(UPDATED_NAME);
        return petType;
    }

    @BeforeEach
    public void initTest() {
        petType = createEntity(em);
    }

    @Test
    @Transactional
    void createPetType() throws Exception {
        int databaseSizeBeforeCreate = petTypeRepository.findAll().size();
        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);
        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPetTypeWithExistingId() throws Exception {
        // Create the PetType with an existing ID
        petType.setId(1L);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        int databaseSizeBeforeCreate = petTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = petTypeRepository.findAll().size();
        // set the field null
        petType.setName(null);

        // Create the PetType, which fails.
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petTypeDTO)))
            .andExpect(status().isBadRequest());

        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPetTypes() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(petType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get the petType
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, petType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(petType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getPetTypesByIdFiltering() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        Long id = petType.getId();

        defaultPetTypeShouldBeFound("id.equals=" + id);
        defaultPetTypeShouldNotBeFound("id.notEquals=" + id);

        defaultPetTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPetTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultPetTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPetTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name equals to DEFAULT_NAME
        defaultPetTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the petTypeList where name equals to UPDATED_NAME
        defaultPetTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPetTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the petTypeList where name equals to UPDATED_NAME
        defaultPetTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name is not null
        defaultPetTypeShouldBeFound("name.specified=true");

        // Get all the petTypeList where name is null
        defaultPetTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPetTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name contains DEFAULT_NAME
        defaultPetTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the petTypeList where name contains UPDATED_NAME
        defaultPetTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name does not contain DEFAULT_NAME
        defaultPetTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the petTypeList where name does not contain UPDATED_NAME
        defaultPetTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPetTypeShouldBeFound(String filter) throws Exception {
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(petType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPetTypeShouldNotBeFound(String filter) throws Exception {
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPetType() throws Exception {
        // Get the petType
        restPetTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType
        PetType updatedPetType = petTypeRepository.findById(petType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPetType are not directly saved in db
        em.detach(updatedPetType);
        updatedPetType.name(UPDATED_NAME);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(updatedPetType);

        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        partialUpdatedPetType.name(UPDATED_NAME);

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
        PetType testPetType = petTypeList.get(petTypeList.size() - 1);
        assertThat(testPetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, petTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPetType() throws Exception {
        int databaseSizeBeforeUpdate = petTypeRepository.findAll().size();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(petTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        int databaseSizeBeforeDelete = petTypeRepository.findAll().size();

        // Delete the petType
        restPetTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, petType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PetType> petTypeList = petTypeRepository.findAll();
        assertThat(petTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
