package org.springframework.samples.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.IntegrationTest;
import org.springframework.samples.petclinic.domain.Owner;
import org.springframework.samples.petclinic.domain.Pet;
import org.springframework.samples.petclinic.domain.PetType;
import org.springframework.samples.petclinic.domain.Visit;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.dto.PetDTO;
import org.springframework.samples.petclinic.service.mapper.PetMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/pets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetMockMvc;

    private Pet pet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createEntity(EntityManager em) {
        Pet pet = new Pet().name(DEFAULT_NAME).birthDate(DEFAULT_BIRTH_DATE);
        return pet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createUpdatedEntity(EntityManager em) {
        Pet pet = new Pet().name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);
        return pet;
    }

    @BeforeEach
    public void initTest() {
        pet = createEntity(em);
    }

    @Test
    @Transactional
    void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();
        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);
        restPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void createPetWithExistingId() throws Exception {
        // Create the Pet with an existing ID
        pet.setId(1L);
        PetDTO petDTO = petMapper.toDto(pet);

        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = petRepository.findAll().size();
        // set the field null
        pet.setName(null);

        // Create the Pet, which fails.
        PetDTO petDTO = petMapper.toDto(pet);

        restPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isBadRequest());

        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPets() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    void getPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get the pet
        restPetMockMvc
            .perform(get(ENTITY_API_URL_ID, pet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    void getPetsByIdFiltering() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        Long id = pet.getId();

        defaultPetShouldBeFound("id.equals=" + id);
        defaultPetShouldNotBeFound("id.notEquals=" + id);

        defaultPetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPetShouldNotBeFound("id.greaterThan=" + id);

        defaultPetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name equals to DEFAULT_NAME
        defaultPetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the petList where name equals to UPDATED_NAME
        defaultPetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the petList where name equals to UPDATED_NAME
        defaultPetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name is not null
        defaultPetShouldBeFound("name.specified=true");

        // Get all the petList where name is null
        defaultPetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPetsByNameContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name contains DEFAULT_NAME
        defaultPetShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the petList where name contains UPDATED_NAME
        defaultPetShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where name does not contain DEFAULT_NAME
        defaultPetShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the petList where name does not contain UPDATED_NAME
        defaultPetShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultPetShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the petList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPetShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultPetShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the petList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPetShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate is not null
        defaultPetShouldBeFound("birthDate.specified=true");

        // Get all the petList where birthDate is null
        defaultPetShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultPetShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the petList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultPetShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultPetShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the petList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultPetShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultPetShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the petList where birthDate is less than UPDATED_BIRTH_DATE
        defaultPetShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPetsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultPetShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the petList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultPetShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPetsByVisitsIsEqualToSomething() throws Exception {
        Visit visits;
        if (TestUtil.findAll(em, Visit.class).isEmpty()) {
            petRepository.saveAndFlush(pet);
            visits = VisitResourceIT.createEntity(em);
        } else {
            visits = TestUtil.findAll(em, Visit.class).get(0);
        }
        em.persist(visits);
        em.flush();
        pet.addVisits(visits);
        petRepository.saveAndFlush(pet);
        Long visitsId = visits.getId();
        // Get all the petList where visits equals to visitsId
        defaultPetShouldBeFound("visitsId.equals=" + visitsId);

        // Get all the petList where visits equals to (visitsId + 1)
        defaultPetShouldNotBeFound("visitsId.equals=" + (visitsId + 1));
    }

    @Test
    @Transactional
    void getAllPetsByTypeIsEqualToSomething() throws Exception {
        PetType type;
        if (TestUtil.findAll(em, PetType.class).isEmpty()) {
            petRepository.saveAndFlush(pet);
            type = PetTypeResourceIT.createEntity(em);
        } else {
            type = TestUtil.findAll(em, PetType.class).get(0);
        }
        em.persist(type);
        em.flush();
        pet.setType(type);
        petRepository.saveAndFlush(pet);
        Long typeId = type.getId();
        // Get all the petList where type equals to typeId
        defaultPetShouldBeFound("typeId.equals=" + typeId);

        // Get all the petList where type equals to (typeId + 1)
        defaultPetShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    @Test
    @Transactional
    void getAllPetsByOwnerIsEqualToSomething() throws Exception {
        Owner owner;
        if (TestUtil.findAll(em, Owner.class).isEmpty()) {
            petRepository.saveAndFlush(pet);
            owner = OwnerResourceIT.createEntity(em);
        } else {
            owner = TestUtil.findAll(em, Owner.class).get(0);
        }
        em.persist(owner);
        em.flush();
        pet.setOwner(owner);
        petRepository.saveAndFlush(pet);
        Long ownerId = owner.getId();
        // Get all the petList where owner equals to ownerId
        defaultPetShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the petList where owner equals to (ownerId + 1)
        defaultPetShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPetShouldBeFound(String filter) throws Exception {
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));

        // Check, that the count call also returns 1
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPetShouldNotBeFound(String filter) throws Exception {
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);
        updatedPet.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);
        PetDTO petDTO = petMapper.toDto(updatedPet);

        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(longCount.incrementAndGet());

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(longCount.incrementAndGet());

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(petDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(longCount.incrementAndGet());

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetWithPatch() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet using partial update
        Pet partialUpdatedPet = new Pet();
        partialUpdatedPet.setId(pet.getId());

        partialUpdatedPet.birthDate(UPDATED_BIRTH_DATE);

        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePetWithPatch() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet using partial update
        Pet partialUpdatedPet = new Pet();
        partialUpdatedPet.setId(pet.getId());

        partialUpdatedPet.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);

        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(longCount.incrementAndGet());

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, petDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(longCount.incrementAndGet());

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(petDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(longCount.incrementAndGet());

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Delete the pet
        restPetMockMvc.perform(delete(ENTITY_API_URL_ID, pet.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
