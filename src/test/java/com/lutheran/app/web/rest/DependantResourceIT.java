package com.lutheran.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lutheran.app.IntegrationTest;
import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.Dependant;
import com.lutheran.app.domain.enumeration.Gender;
import com.lutheran.app.repository.DependantRepository;
import com.lutheran.app.service.DependantService;
import com.lutheran.app.service.dto.DependantDTO;
import com.lutheran.app.service.mapper.DependantMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DependantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DependantResourceIT {

    private static final String DEFAULT_FULL_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAMES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String ENTITY_API_URL = "/api/dependants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DependantRepository dependantRepository;

    @Mock
    private DependantRepository dependantRepositoryMock;

    @Autowired
    private DependantMapper dependantMapper;

    @Mock
    private DependantService dependantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependantMockMvc;

    private Dependant dependant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependant createEntity(EntityManager em) {
        Dependant dependant = new Dependant().fullNames(DEFAULT_FULL_NAMES).dateOfBirth(DEFAULT_DATE_OF_BIRTH).gender(DEFAULT_GENDER);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        dependant.setCongregant(congregant);
        return dependant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependant createUpdatedEntity(EntityManager em) {
        Dependant dependant = new Dependant().fullNames(UPDATED_FULL_NAMES).dateOfBirth(UPDATED_DATE_OF_BIRTH).gender(UPDATED_GENDER);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createUpdatedEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        dependant.setCongregant(congregant);
        return dependant;
    }

    @BeforeEach
    public void initTest() {
        dependant = createEntity(em);
    }

    @Test
    @Transactional
    void createDependant() throws Exception {
        int databaseSizeBeforeCreate = dependantRepository.findAll().size();
        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);
        restDependantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependantDTO)))
            .andExpect(status().isCreated());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeCreate + 1);
        Dependant testDependant = dependantList.get(dependantList.size() - 1);
        assertThat(testDependant.getFullNames()).isEqualTo(DEFAULT_FULL_NAMES);
        assertThat(testDependant.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testDependant.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void createDependantWithExistingId() throws Exception {
        // Create the Dependant with an existing ID
        dependant.setId(1L);
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        int databaseSizeBeforeCreate = dependantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNamesIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependantRepository.findAll().size();
        // set the field null
        dependant.setFullNames(null);

        // Create the Dependant, which fails.
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        restDependantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependantDTO)))
            .andExpect(status().isBadRequest());

        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependantRepository.findAll().size();
        // set the field null
        dependant.setDateOfBirth(null);

        // Create the Dependant, which fails.
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        restDependantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependantDTO)))
            .andExpect(status().isBadRequest());

        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependantRepository.findAll().size();
        // set the field null
        dependant.setGender(null);

        // Create the Dependant, which fails.
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        restDependantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependantDTO)))
            .andExpect(status().isBadRequest());

        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDependants() throws Exception {
        // Initialize the database
        dependantRepository.saveAndFlush(dependant);

        // Get all the dependantList
        restDependantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependant.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullNames").value(hasItem(DEFAULT_FULL_NAMES)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDependantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(dependantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDependantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dependantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDependantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dependantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDependantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dependantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDependant() throws Exception {
        // Initialize the database
        dependantRepository.saveAndFlush(dependant);

        // Get the dependant
        restDependantMockMvc
            .perform(get(ENTITY_API_URL_ID, dependant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependant.getId().intValue()))
            .andExpect(jsonPath("$.fullNames").value(DEFAULT_FULL_NAMES))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDependant() throws Exception {
        // Get the dependant
        restDependantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDependant() throws Exception {
        // Initialize the database
        dependantRepository.saveAndFlush(dependant);

        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();

        // Update the dependant
        Dependant updatedDependant = dependantRepository.findById(dependant.getId()).get();
        // Disconnect from session so that the updates on updatedDependant are not directly saved in db
        em.detach(updatedDependant);
        updatedDependant.fullNames(UPDATED_FULL_NAMES).dateOfBirth(UPDATED_DATE_OF_BIRTH).gender(UPDATED_GENDER);
        DependantDTO dependantDTO = dependantMapper.toDto(updatedDependant);

        restDependantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
        Dependant testDependant = dependantList.get(dependantList.size() - 1);
        assertThat(testDependant.getFullNames()).isEqualTo(UPDATED_FULL_NAMES);
        assertThat(testDependant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testDependant.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void putNonExistingDependant() throws Exception {
        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();
        dependant.setId(count.incrementAndGet());

        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDependant() throws Exception {
        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();
        dependant.setId(count.incrementAndGet());

        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDependant() throws Exception {
        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();
        dependant.setId(count.incrementAndGet());

        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDependantWithPatch() throws Exception {
        // Initialize the database
        dependantRepository.saveAndFlush(dependant);

        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();

        // Update the dependant using partial update
        Dependant partialUpdatedDependant = new Dependant();
        partialUpdatedDependant.setId(dependant.getId());

        restDependantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependant))
            )
            .andExpect(status().isOk());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
        Dependant testDependant = dependantList.get(dependantList.size() - 1);
        assertThat(testDependant.getFullNames()).isEqualTo(DEFAULT_FULL_NAMES);
        assertThat(testDependant.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testDependant.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateDependantWithPatch() throws Exception {
        // Initialize the database
        dependantRepository.saveAndFlush(dependant);

        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();

        // Update the dependant using partial update
        Dependant partialUpdatedDependant = new Dependant();
        partialUpdatedDependant.setId(dependant.getId());

        partialUpdatedDependant.fullNames(UPDATED_FULL_NAMES).dateOfBirth(UPDATED_DATE_OF_BIRTH).gender(UPDATED_GENDER);

        restDependantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependant))
            )
            .andExpect(status().isOk());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
        Dependant testDependant = dependantList.get(dependantList.size() - 1);
        assertThat(testDependant.getFullNames()).isEqualTo(UPDATED_FULL_NAMES);
        assertThat(testDependant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testDependant.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingDependant() throws Exception {
        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();
        dependant.setId(count.incrementAndGet());

        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dependantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDependant() throws Exception {
        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();
        dependant.setId(count.incrementAndGet());

        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDependant() throws Exception {
        int databaseSizeBeforeUpdate = dependantRepository.findAll().size();
        dependant.setId(count.incrementAndGet());

        // Create the Dependant
        DependantDTO dependantDTO = dependantMapper.toDto(dependant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dependantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependant in the database
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDependant() throws Exception {
        // Initialize the database
        dependantRepository.saveAndFlush(dependant);

        int databaseSizeBeforeDelete = dependantRepository.findAll().size();

        // Delete the dependant
        restDependantMockMvc
            .perform(delete(ENTITY_API_URL_ID, dependant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependant> dependantList = dependantRepository.findAll();
        assertThat(dependantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
