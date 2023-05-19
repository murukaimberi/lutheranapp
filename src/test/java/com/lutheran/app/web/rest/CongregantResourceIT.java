package com.lutheran.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lutheran.app.IntegrationTest;
import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.User;
import com.lutheran.app.domain.enumeration.Gender;
import com.lutheran.app.domain.enumeration.MaritalStatus;
import com.lutheran.app.repository.CongregantRepository;
import com.lutheran.app.service.CongregantService;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.mapper.CongregantMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CongregantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CongregantResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.MARRIED;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.DEVORCED;

    private static final byte[] DEFAULT_PROFILE_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PICTURE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/congregants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CongregantRepository congregantRepository;

    @Mock
    private CongregantRepository congregantRepositoryMock;

    @Autowired
    private CongregantMapper congregantMapper;

    @Mock
    private CongregantService congregantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCongregantMockMvc;

    private Congregant congregant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Congregant createEntity(EntityManager em) {
        Congregant congregant = new Congregant()
            .title(DEFAULT_TITLE)
            .firstNames(DEFAULT_FIRST_NAMES)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .dob(DEFAULT_DOB)
            .gender(DEFAULT_GENDER)
            .profession(DEFAULT_PROFESSION)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .profilePicture(DEFAULT_PROFILE_PICTURE)
            .profilePictureContentType(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        congregant.setUser(user);
        return congregant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Congregant createUpdatedEntity(EntityManager em) {
        Congregant congregant = new Congregant()
            .title(UPDATED_TITLE)
            .firstNames(UPDATED_FIRST_NAMES)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .profession(UPDATED_PROFESSION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .profilePictureContentType(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        congregant.setUser(user);
        return congregant;
    }

    @BeforeEach
    public void initTest() {
        congregant = createEntity(em);
    }

    @Test
    @Transactional
    void createCongregant() throws Exception {
        int databaseSizeBeforeCreate = congregantRepository.findAll().size();
        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);
        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isCreated());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeCreate + 1);
        Congregant testCongregant = congregantList.get(congregantList.size() - 1);
        assertThat(testCongregant.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCongregant.getFirstNames()).isEqualTo(DEFAULT_FIRST_NAMES);
        assertThat(testCongregant.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testCongregant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCongregant.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testCongregant.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCongregant.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testCongregant.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testCongregant.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
        assertThat(testCongregant.getProfilePictureContentType()).isEqualTo(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createCongregantWithExistingId() throws Exception {
        // Create the Congregant with an existing ID
        congregant.setId(1L);
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        int databaseSizeBeforeCreate = congregantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = congregantRepository.findAll().size();
        // set the field null
        congregant.setTitle(null);

        // Create the Congregant, which fails.
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNamesIsRequired() throws Exception {
        int databaseSizeBeforeTest = congregantRepository.findAll().size();
        // set the field null
        congregant.setFirstNames(null);

        // Create the Congregant, which fails.
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = congregantRepository.findAll().size();
        // set the field null
        congregant.setSurname(null);

        // Create the Congregant, which fails.
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = congregantRepository.findAll().size();
        // set the field null
        congregant.setEmail(null);

        // Create the Congregant, which fails.
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = congregantRepository.findAll().size();
        // set the field null
        congregant.setDob(null);

        // Create the Congregant, which fails.
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaritalStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = congregantRepository.findAll().size();
        // set the field null
        congregant.setMaritalStatus(null);

        // Create the Congregant, which fails.
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        restCongregantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isBadRequest());

        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCongregants() throws Exception {
        // Initialize the database
        congregantRepository.saveAndFlush(congregant);

        // Get all the congregantList
        restCongregantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(congregant.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstNames").value(hasItem(DEFAULT_FIRST_NAMES)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].profilePictureContentType").value(hasItem(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCongregantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(congregantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCongregantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(congregantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCongregantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(congregantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCongregantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(congregantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCongregant() throws Exception {
        // Initialize the database
        congregantRepository.saveAndFlush(congregant);

        // Get the congregant
        restCongregantMockMvc
            .perform(get(ENTITY_API_URL_ID, congregant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(congregant.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.firstNames").value(DEFAULT_FIRST_NAMES))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.profilePictureContentType").value(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePicture").value(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE)));
    }

    @Test
    @Transactional
    void getNonExistingCongregant() throws Exception {
        // Get the congregant
        restCongregantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCongregant() throws Exception {
        // Initialize the database
        congregantRepository.saveAndFlush(congregant);

        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();

        // Update the congregant
        Congregant updatedCongregant = congregantRepository.findById(congregant.getId()).get();
        // Disconnect from session so that the updates on updatedCongregant are not directly saved in db
        em.detach(updatedCongregant);
        updatedCongregant
            .title(UPDATED_TITLE)
            .firstNames(UPDATED_FIRST_NAMES)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .profession(UPDATED_PROFESSION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .profilePictureContentType(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);
        CongregantDTO congregantDTO = congregantMapper.toDto(updatedCongregant);

        restCongregantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, congregantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(congregantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
        Congregant testCongregant = congregantList.get(congregantList.size() - 1);
        assertThat(testCongregant.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCongregant.getFirstNames()).isEqualTo(UPDATED_FIRST_NAMES);
        assertThat(testCongregant.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testCongregant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCongregant.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCongregant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCongregant.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testCongregant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testCongregant.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
        assertThat(testCongregant.getProfilePictureContentType()).isEqualTo(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCongregant() throws Exception {
        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();
        congregant.setId(count.incrementAndGet());

        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCongregantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, congregantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(congregantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCongregant() throws Exception {
        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();
        congregant.setId(count.incrementAndGet());

        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongregantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(congregantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCongregant() throws Exception {
        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();
        congregant.setId(count.incrementAndGet());

        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongregantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(congregantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCongregantWithPatch() throws Exception {
        // Initialize the database
        congregantRepository.saveAndFlush(congregant);

        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();

        // Update the congregant using partial update
        Congregant partialUpdatedCongregant = new Congregant();
        partialUpdatedCongregant.setId(congregant.getId());

        partialUpdatedCongregant.surname(UPDATED_SURNAME).email(UPDATED_EMAIL).dob(UPDATED_DOB);

        restCongregantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCongregant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCongregant))
            )
            .andExpect(status().isOk());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
        Congregant testCongregant = congregantList.get(congregantList.size() - 1);
        assertThat(testCongregant.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCongregant.getFirstNames()).isEqualTo(DEFAULT_FIRST_NAMES);
        assertThat(testCongregant.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testCongregant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCongregant.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCongregant.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCongregant.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testCongregant.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testCongregant.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
        assertThat(testCongregant.getProfilePictureContentType()).isEqualTo(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCongregantWithPatch() throws Exception {
        // Initialize the database
        congregantRepository.saveAndFlush(congregant);

        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();

        // Update the congregant using partial update
        Congregant partialUpdatedCongregant = new Congregant();
        partialUpdatedCongregant.setId(congregant.getId());

        partialUpdatedCongregant
            .title(UPDATED_TITLE)
            .firstNames(UPDATED_FIRST_NAMES)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .dob(UPDATED_DOB)
            .gender(UPDATED_GENDER)
            .profession(UPDATED_PROFESSION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .profilePictureContentType(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);

        restCongregantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCongregant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCongregant))
            )
            .andExpect(status().isOk());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
        Congregant testCongregant = congregantList.get(congregantList.size() - 1);
        assertThat(testCongregant.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCongregant.getFirstNames()).isEqualTo(UPDATED_FIRST_NAMES);
        assertThat(testCongregant.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testCongregant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCongregant.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCongregant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCongregant.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testCongregant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testCongregant.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
        assertThat(testCongregant.getProfilePictureContentType()).isEqualTo(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCongregant() throws Exception {
        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();
        congregant.setId(count.incrementAndGet());

        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCongregantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, congregantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(congregantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCongregant() throws Exception {
        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();
        congregant.setId(count.incrementAndGet());

        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongregantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(congregantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCongregant() throws Exception {
        int databaseSizeBeforeUpdate = congregantRepository.findAll().size();
        congregant.setId(count.incrementAndGet());

        // Create the Congregant
        CongregantDTO congregantDTO = congregantMapper.toDto(congregant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCongregantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(congregantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Congregant in the database
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCongregant() throws Exception {
        // Initialize the database
        congregantRepository.saveAndFlush(congregant);

        int databaseSizeBeforeDelete = congregantRepository.findAll().size();

        // Delete the congregant
        restCongregantMockMvc
            .perform(delete(ENTITY_API_URL_ID, congregant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Congregant> congregantList = congregantRepository.findAll();
        assertThat(congregantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
