package org.springframework.samples.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.samples.petclinic.domain.VetAsserts.*;
import static org.springframework.samples.petclinic.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.samples.petclinic.web.rest.TestUtil.sameNumber;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.IntegrationTest;
import org.springframework.samples.petclinic.domain.Speciality;
import org.springframework.samples.petclinic.domain.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.dto.VetDTO;
import org.springframework.samples.petclinic.service.mapper.VetMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VetResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALARY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/vets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VetRepository vetRepository;

    @Mock
    private VetRepository vetRepositoryMock;

    @Autowired
    private VetMapper vetMapper;

    @Mock
    private VetService vetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVetMockMvc;

    private Vet vet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createEntity(EntityManager em) {
        Vet vet = new Vet().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME).salary(DEFAULT_SALARY);
        return vet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createUpdatedEntity(EntityManager em) {
        Vet vet = new Vet().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).salary(UPDATED_SALARY);
        return vet;
    }

    @BeforeEach
    public void initTest() {
        vet = createEntity(em);
    }

    @Test
    @Transactional
    void createVet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);
        var returnedVetDTO = om.readValue(
            restVetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VetDTO.class
        );

        // Validate the Vet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVet = vetMapper.toEntity(returnedVetDTO);
        assertVetUpdatableFieldsEquals(returnedVet, getPersistedVet(returnedVet));
    }

    @Test
    @Transactional
    void createVetWithExistingId() throws Exception {
        // Create the Vet with an existing ID
        vet.setId(1L);
        VetDTO vetDTO = vetMapper.toDto(vet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vet.setFirstName(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vet.setLastName(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vet.setSalary(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVets() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vetRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get the vet
        restVetMockMvc
            .perform(get(ENTITY_API_URL_ID, vet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vet.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.salary").value(sameNumber(DEFAULT_SALARY)));
    }

    @Test
    @Transactional
    void getVetsByIdFiltering() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        Long id = vet.getId();

        defaultVetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName equals to
        defaultVetFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName in
        defaultVetFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName is not null
        defaultVetFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName contains
        defaultVetFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName does not contain
        defaultVetFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName equals to
        defaultVetFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName in
        defaultVetFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName is not null
        defaultVetFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName contains
        defaultVetFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName does not contain
        defaultVetFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary equals to
        defaultVetFiltering("salary.equals=" + DEFAULT_SALARY, "salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary in
        defaultVetFiltering("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY, "salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary is not null
        defaultVetFiltering("salary.specified=true", "salary.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary is greater than or equal to
        defaultVetFiltering("salary.greaterThanOrEqual=" + DEFAULT_SALARY, "salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary is less than or equal to
        defaultVetFiltering("salary.lessThanOrEqual=" + DEFAULT_SALARY, "salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary is less than
        defaultVetFiltering("salary.lessThan=" + UPDATED_SALARY, "salary.lessThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllVetsBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where salary is greater than
        defaultVetFiltering("salary.greaterThan=" + SMALLER_SALARY, "salary.greaterThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllVetsBySpecialitiesIsEqualToSomething() throws Exception {
        Speciality specialities;
        if (TestUtil.findAll(em, Speciality.class).isEmpty()) {
            vetRepository.saveAndFlush(vet);
            specialities = SpecialityResourceIT.createEntity(em);
        } else {
            specialities = TestUtil.findAll(em, Speciality.class).get(0);
        }
        em.persist(specialities);
        em.flush();
        vet.addSpecialities(specialities);
        vetRepository.saveAndFlush(vet);
        Long specialitiesId = specialities.getId();
        // Get all the vetList where specialities equals to specialitiesId
        defaultVetShouldBeFound("specialitiesId.equals=" + specialitiesId);

        // Get all the vetList where specialities equals to (specialitiesId + 1)
        defaultVetShouldNotBeFound("specialitiesId.equals=" + (specialitiesId + 1));
    }

    private void defaultVetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVetShouldBeFound(shouldBeFound);
        defaultVetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVetShouldBeFound(String filter) throws Exception {
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))));

        // Check, that the count call also returns 1
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVetShouldNotBeFound(String filter) throws Exception {
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVet() throws Exception {
        // Get the vet
        restVetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vet
        Vet updatedVet = vetRepository.findById(vet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVet are not directly saved in db
        em.detach(updatedVet);
        updatedVet.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).salary(UPDATED_SALARY);
        VetDTO vetDTO = vetMapper.toDto(updatedVet);

        restVetMockMvc
            .perform(put(ENTITY_API_URL_ID, vetDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isOk());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVetToMatchAllProperties(updatedVet);
    }

    @Test
    @Transactional
    void putNonExistingVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(put(ENTITY_API_URL_ID, vetDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVetWithPatch() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.lastName(UPDATED_LAST_NAME);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVet, vet), getPersistedVet(vet));
    }

    @Test
    @Transactional
    void fullUpdateVetWithPatch() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).salary(UPDATED_SALARY);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVetUpdatableFieldsEquals(partialUpdatedVet, getPersistedVet(partialUpdatedVet));
    }

    @Test
    @Transactional
    void patchNonExistingVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vetDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vet
        restVetMockMvc.perform(delete(ENTITY_API_URL_ID, vet.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vetRepository.count();
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

    protected Vet getPersistedVet(Vet vet) {
        return vetRepository.findById(vet.getId()).orElseThrow();
    }

    protected void assertPersistedVetToMatchAllProperties(Vet expectedVet) {
        assertVetAllPropertiesEquals(expectedVet, getPersistedVet(expectedVet));
    }

    protected void assertPersistedVetToMatchUpdatableProperties(Vet expectedVet) {
        assertVetAllUpdatablePropertiesEquals(expectedVet, getPersistedVet(expectedVet));
    }
}
