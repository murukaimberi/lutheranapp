package com.lutheran.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lutheran.app.IntegrationTest;
import com.lutheran.app.domain.BaptismHistory;
import com.lutheran.app.domain.Congregant;
import com.lutheran.app.repository.BaptismHistoryRepository;
import com.lutheran.app.service.dto.BaptismHistoryDTO;
import com.lutheran.app.service.mapper.BaptismHistoryMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BaptismHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaptismHistoryResourceIT {

    private static final String DEFAULT_PARISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARISH_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BAPTISM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BAPTISM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CONFIRMED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONFIRMED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PARISH_BAPTISED_AT = "AAAAAAAAAA";
    private static final String UPDATED_PARISH_BAPTISED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_PRISHED_CONFIRMED_AT = "AAAAAAAAAA";
    private static final String UPDATED_PRISHED_CONFIRMED_AT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/baptism-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BaptismHistoryRepository baptismHistoryRepository;

    @Autowired
    private BaptismHistoryMapper baptismHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaptismHistoryMockMvc;

    private BaptismHistory baptismHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaptismHistory createEntity(EntityManager em) {
        BaptismHistory baptismHistory = new BaptismHistory()
            .parishName(DEFAULT_PARISH_NAME)
            .baptismDate(DEFAULT_BAPTISM_DATE)
            .confirmedDate(DEFAULT_CONFIRMED_DATE)
            .parishBaptisedAt(DEFAULT_PARISH_BAPTISED_AT)
            .prishedConfirmedAt(DEFAULT_PRISHED_CONFIRMED_AT);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        baptismHistory.setCongragant(congregant);
        return baptismHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaptismHistory createUpdatedEntity(EntityManager em) {
        BaptismHistory baptismHistory = new BaptismHistory()
            .parishName(UPDATED_PARISH_NAME)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .confirmedDate(UPDATED_CONFIRMED_DATE)
            .parishBaptisedAt(UPDATED_PARISH_BAPTISED_AT)
            .prishedConfirmedAt(UPDATED_PRISHED_CONFIRMED_AT);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createUpdatedEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        baptismHistory.setCongragant(congregant);
        return baptismHistory;
    }

    @BeforeEach
    public void initTest() {
        baptismHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createBaptismHistory() throws Exception {
        int databaseSizeBeforeCreate = baptismHistoryRepository.findAll().size();
        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);
        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getParishName()).isEqualTo(DEFAULT_PARISH_NAME);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(DEFAULT_BAPTISM_DATE);
        assertThat(testBaptismHistory.getConfirmedDate()).isEqualTo(DEFAULT_CONFIRMED_DATE);
        assertThat(testBaptismHistory.getParishBaptisedAt()).isEqualTo(DEFAULT_PARISH_BAPTISED_AT);
        assertThat(testBaptismHistory.getPrishedConfirmedAt()).isEqualTo(DEFAULT_PRISHED_CONFIRMED_AT);
    }

    @Test
    @Transactional
    void createBaptismHistoryWithExistingId() throws Exception {
        // Create the BaptismHistory with an existing ID
        baptismHistory.setId(1L);
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        int databaseSizeBeforeCreate = baptismHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkParishNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = baptismHistoryRepository.findAll().size();
        // set the field null
        baptismHistory.setParishName(null);

        // Create the BaptismHistory, which fails.
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBaptismDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = baptismHistoryRepository.findAll().size();
        // set the field null
        baptismHistory.setBaptismDate(null);

        // Create the BaptismHistory, which fails.
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfirmedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = baptismHistoryRepository.findAll().size();
        // set the field null
        baptismHistory.setConfirmedDate(null);

        // Create the BaptismHistory, which fails.
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParishBaptisedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = baptismHistoryRepository.findAll().size();
        // set the field null
        baptismHistory.setParishBaptisedAt(null);

        // Create the BaptismHistory, which fails.
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrishedConfirmedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = baptismHistoryRepository.findAll().size();
        // set the field null
        baptismHistory.setPrishedConfirmedAt(null);

        // Create the BaptismHistory, which fails.
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBaptismHistories() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        // Get all the baptismHistoryList
        restBaptismHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baptismHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].parishName").value(hasItem(DEFAULT_PARISH_NAME)))
            .andExpect(jsonPath("$.[*].baptismDate").value(hasItem(DEFAULT_BAPTISM_DATE.toString())))
            .andExpect(jsonPath("$.[*].confirmedDate").value(hasItem(DEFAULT_CONFIRMED_DATE.toString())))
            .andExpect(jsonPath("$.[*].parishBaptisedAt").value(hasItem(DEFAULT_PARISH_BAPTISED_AT)))
            .andExpect(jsonPath("$.[*].prishedConfirmedAt").value(hasItem(DEFAULT_PRISHED_CONFIRMED_AT)));
    }

    @Test
    @Transactional
    void getBaptismHistory() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        // Get the baptismHistory
        restBaptismHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, baptismHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baptismHistory.getId().intValue()))
            .andExpect(jsonPath("$.parishName").value(DEFAULT_PARISH_NAME))
            .andExpect(jsonPath("$.baptismDate").value(DEFAULT_BAPTISM_DATE.toString()))
            .andExpect(jsonPath("$.confirmedDate").value(DEFAULT_CONFIRMED_DATE.toString()))
            .andExpect(jsonPath("$.parishBaptisedAt").value(DEFAULT_PARISH_BAPTISED_AT))
            .andExpect(jsonPath("$.prishedConfirmedAt").value(DEFAULT_PRISHED_CONFIRMED_AT));
    }

    @Test
    @Transactional
    void getNonExistingBaptismHistory() throws Exception {
        // Get the baptismHistory
        restBaptismHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBaptismHistory() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();

        // Update the baptismHistory
        BaptismHistory updatedBaptismHistory = baptismHistoryRepository.findById(baptismHistory.getId()).get();
        // Disconnect from session so that the updates on updatedBaptismHistory are not directly saved in db
        em.detach(updatedBaptismHistory);
        updatedBaptismHistory
            .parishName(UPDATED_PARISH_NAME)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .confirmedDate(UPDATED_CONFIRMED_DATE)
            .parishBaptisedAt(UPDATED_PARISH_BAPTISED_AT)
            .prishedConfirmedAt(UPDATED_PRISHED_CONFIRMED_AT);
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(updatedBaptismHistory);

        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baptismHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getParishName()).isEqualTo(UPDATED_PARISH_NAME);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(UPDATED_BAPTISM_DATE);
        assertThat(testBaptismHistory.getConfirmedDate()).isEqualTo(UPDATED_CONFIRMED_DATE);
        assertThat(testBaptismHistory.getParishBaptisedAt()).isEqualTo(UPDATED_PARISH_BAPTISED_AT);
        assertThat(testBaptismHistory.getPrishedConfirmedAt()).isEqualTo(UPDATED_PRISHED_CONFIRMED_AT);
    }

    @Test
    @Transactional
    void putNonExistingBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baptismHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaptismHistoryWithPatch() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();

        // Update the baptismHistory using partial update
        BaptismHistory partialUpdatedBaptismHistory = new BaptismHistory();
        partialUpdatedBaptismHistory.setId(baptismHistory.getId());

        partialUpdatedBaptismHistory
            .parishName(UPDATED_PARISH_NAME)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .confirmedDate(UPDATED_CONFIRMED_DATE);

        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaptismHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaptismHistory))
            )
            .andExpect(status().isOk());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getParishName()).isEqualTo(UPDATED_PARISH_NAME);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(UPDATED_BAPTISM_DATE);
        assertThat(testBaptismHistory.getConfirmedDate()).isEqualTo(UPDATED_CONFIRMED_DATE);
        assertThat(testBaptismHistory.getParishBaptisedAt()).isEqualTo(DEFAULT_PARISH_BAPTISED_AT);
        assertThat(testBaptismHistory.getPrishedConfirmedAt()).isEqualTo(DEFAULT_PRISHED_CONFIRMED_AT);
    }

    @Test
    @Transactional
    void fullUpdateBaptismHistoryWithPatch() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();

        // Update the baptismHistory using partial update
        BaptismHistory partialUpdatedBaptismHistory = new BaptismHistory();
        partialUpdatedBaptismHistory.setId(baptismHistory.getId());

        partialUpdatedBaptismHistory
            .parishName(UPDATED_PARISH_NAME)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .confirmedDate(UPDATED_CONFIRMED_DATE)
            .parishBaptisedAt(UPDATED_PARISH_BAPTISED_AT)
            .prishedConfirmedAt(UPDATED_PRISHED_CONFIRMED_AT);

        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaptismHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaptismHistory))
            )
            .andExpect(status().isOk());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getParishName()).isEqualTo(UPDATED_PARISH_NAME);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(UPDATED_BAPTISM_DATE);
        assertThat(testBaptismHistory.getConfirmedDate()).isEqualTo(UPDATED_CONFIRMED_DATE);
        assertThat(testBaptismHistory.getParishBaptisedAt()).isEqualTo(UPDATED_PARISH_BAPTISED_AT);
        assertThat(testBaptismHistory.getPrishedConfirmedAt()).isEqualTo(UPDATED_PRISHED_CONFIRMED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baptismHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // Create the BaptismHistory
        BaptismHistoryDTO baptismHistoryDTO = baptismHistoryMapper.toDto(baptismHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaptismHistory() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeDelete = baptismHistoryRepository.findAll().size();

        // Delete the baptismHistory
        restBaptismHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, baptismHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
