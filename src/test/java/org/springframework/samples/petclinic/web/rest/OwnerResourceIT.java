package org.springframework.samples.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.samples.petclinic.domain.OwnerAsserts.*;
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
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.service.dto.OwnerDTO;
import org.springframework.samples.petclinic.service.mapper.OwnerMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnerMockMvc;

    private Owner owner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createEntity(EntityManager em) {
        Owner owner = new Owner()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .telephone(DEFAULT_TELEPHONE);
        return owner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createUpdatedEntity(EntityManager em) {
        Owner owner = new Owner()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        return owner;
    }

    @BeforeEach
    public void initTest() {
        owner = createEntity(em);
    }

    @Test
    @Transactional
    void createOwner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);
        var returnedOwnerDTO = om.readValue(
            restOwnerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OwnerDTO.class
        );

        // Validate the Owner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOwner = ownerMapper.toEntity(returnedOwnerDTO);
        assertOwnerUpdatableFieldsEquals(returnedOwner, getPersistedOwner(returnedOwner));
    }

    @Test
    @Transactional
    void createOwnerWithExistingId() throws Exception {
        // Create the Owner with an existing ID
        owner.setId(1L);
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        owner.setFirstName(null);

        // Create the Owner, which fails.
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        owner.setLastName(null);

        // Create the Owner, which fails.
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        restOwnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getOwnersByIdFiltering() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        Long id = owner.getId();

        defaultOwnerFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOwnerFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOwnerFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOwnersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where firstName equals to
        defaultOwnerFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where firstName in
        defaultOwnerFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where firstName is not null
        defaultOwnerFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where firstName contains
        defaultOwnerFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where firstName does not contain
        defaultOwnerFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where lastName equals to
        defaultOwnerFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where lastName in
        defaultOwnerFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where lastName is not null
        defaultOwnerFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where lastName contains
        defaultOwnerFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where lastName does not contain
        defaultOwnerFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email equals to
        defaultOwnerFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email in
        defaultOwnerFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email is not null
        defaultOwnerFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByEmailContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email contains
        defaultOwnerFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where email does not contain
        defaultOwnerFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllOwnersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where address equals to
        defaultOwnerFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOwnersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where address in
        defaultOwnerFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOwnersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where address is not null
        defaultOwnerFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByAddressContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where address contains
        defaultOwnerFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOwnersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where address does not contain
        defaultOwnerFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOwnersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where city equals to
        defaultOwnerFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where city in
        defaultOwnerFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where city is not null
        defaultOwnerFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByCityContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where city contains
        defaultOwnerFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where city does not contain
        defaultOwnerFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllOwnersByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where telephone equals to
        defaultOwnerFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where telephone in
        defaultOwnerFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where telephone is not null
        defaultOwnerFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllOwnersByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where telephone contains
        defaultOwnerFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOwnersByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList where telephone does not contain
        defaultOwnerFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    private void defaultOwnerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOwnerShouldBeFound(shouldBeFound);
        defaultOwnerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOwnerShouldBeFound(String filter) throws Exception {
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));

        // Check, that the count call also returns 1
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOwnerShouldNotBeFound(String filter) throws Exception {
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOwner are not directly saved in db
        em.detach(updatedOwner);
        updatedOwner
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);
        OwnerDTO ownerDTO = ownerMapper.toDto(updatedOwner);

        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOwnerToMatchAllProperties(updatedOwner);
    }

    @Test
    @Transactional
    void putNonExistingOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        owner.setId(longCount.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ownerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        owner.setId(longCount.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        owner.setId(longCount.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ownerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner.lastName(UPDATED_LAST_NAME).address(UPDATED_ADDRESS);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOwnerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOwner, owner), getPersistedOwner(owner));
    }

    @Test
    @Transactional
    void fullUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .telephone(UPDATED_TELEPHONE);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOwnerUpdatableFieldsEquals(partialUpdatedOwner, getPersistedOwner(partialUpdatedOwner));
    }

    @Test
    @Transactional
    void patchNonExistingOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        owner.setId(longCount.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ownerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        owner.setId(longCount.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ownerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        owner.setId(longCount.incrementAndGet());

        // Create the Owner
        OwnerDTO ownerDTO = ownerMapper.toDto(owner);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ownerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the owner
        restOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, owner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ownerRepository.count();
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

    protected Owner getPersistedOwner(Owner owner) {
        return ownerRepository.findById(owner.getId()).orElseThrow();
    }

    protected void assertPersistedOwnerToMatchAllProperties(Owner expectedOwner) {
        assertOwnerAllPropertiesEquals(expectedOwner, getPersistedOwner(expectedOwner));
    }

    protected void assertPersistedOwnerToMatchUpdatableProperties(Owner expectedOwner) {
        assertOwnerAllUpdatablePropertiesEquals(expectedOwner, getPersistedOwner(expectedOwner));
    }
}
