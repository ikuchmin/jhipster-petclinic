package org.springframework.samples.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.samples.petclinic.domain.PetTypeAsserts.*;
import static org.springframework.samples.petclinic.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
    private ObjectMapper om;

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
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);
        var returnedPetTypeDTO = om.readValue(
            restPetTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PetTypeDTO.class
        );

        // Validate the PetType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPetType = petTypeMapper.toEntity(returnedPetTypeDTO);
        assertPetTypeUpdatableFieldsEquals(returnedPetType, getPersistedPetType(returnedPetType));
    }

    @Test
    @Transactional
    void createPetTypeWithExistingId() throws Exception {
        // Create the PetType with an existing ID
        petType.setId(1L);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        petType.setName(null);

        // Create the PetType, which fails.
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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

        defaultPetTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPetTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPetTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name equals to
        defaultPetTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name in
        defaultPetTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name is not null
        defaultPetTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPetTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name contains
        defaultPetTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList where name does not contain
        defaultPetTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultPetTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPetTypeShouldBeFound(shouldBeFound);
        defaultPetTypeShouldNotBeFound(shouldNotBeFound);
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

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the petType
        PetType updatedPetType = petTypeRepository.findById(petType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPetType are not directly saved in db
        em.detach(updatedPetType);
        updatedPetType.name(UPDATED_NAME);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(updatedPetType);

        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petTypeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPetTypeToMatchAllProperties(updatedPetType);
    }

    @Test
    @Transactional
    void putNonExistingPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petTypeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPetTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPetType, petType), getPersistedPetType(petType));
    }

    @Test
    @Transactional
    void fullUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        partialUpdatedPetType.name(UPDATED_NAME);

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPetTypeUpdatableFieldsEquals(partialUpdatedPetType, getPersistedPetType(partialUpdatedPetType));
    }

    @Test
    @Transactional
    void patchNonExistingPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, petTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePetType() throws Exception {
        // Initialize the database
        petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the petType
        restPetTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, petType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return petTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PetType getPersistedPetType(PetType petType) {
        return petTypeRepository.findById(petType.getId()).orElseThrow();
    }

    protected void assertPersistedPetTypeToMatchAllProperties(PetType expectedPetType) {
        assertPetTypeAllPropertiesEquals(expectedPetType, getPersistedPetType(expectedPetType));
    }

    protected void assertPersistedPetTypeToMatchUpdatableProperties(PetType expectedPetType) {
        assertPetTypeAllUpdatablePropertiesEquals(expectedPetType, getPersistedPetType(expectedPetType));
    }
}
