package com.lutheran.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lutheran.app.IntegrationTest;
import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.MarriageHistory;
import com.lutheran.app.repository.MarriageHistoryRepository;
import com.lutheran.app.service.dto.MarriageHistoryDTO;
import com.lutheran.app.service.mapper.MarriageHistoryMapper;
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
 * Integration tests for the {@link MarriageHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarriageHistoryResourceIT {

    private static final LocalDate DEFAULT_MARRIAGE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MARRIAGE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PARISH_MARRIED_AT = "AAAAAAAAAA";
    private static final String UPDATED_PARISH_MARRIED_AT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/marriage-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarriageHistoryRepository marriageHistoryRepository;

    @Autowired
    private MarriageHistoryMapper marriageHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarriageHistoryMockMvc;

    private MarriageHistory marriageHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarriageHistory createEntity(EntityManager em) {
        MarriageHistory marriageHistory = new MarriageHistory()
            .marriageDate(DEFAULT_MARRIAGE_DATE)
            .parishMarriedAt(DEFAULT_PARISH_MARRIED_AT);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        marriageHistory.setCongregant(congregant);
        return marriageHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarriageHistory createUpdatedEntity(EntityManager em) {
        MarriageHistory marriageHistory = new MarriageHistory()
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .parishMarriedAt(UPDATED_PARISH_MARRIED_AT);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createUpdatedEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        marriageHistory.setCongregant(congregant);
        return marriageHistory;
    }

    @BeforeEach
    public void initTest() {
        marriageHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createMarriageHistory() throws Exception {
        int databaseSizeBeforeCreate = marriageHistoryRepository.findAll().size();
        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);
        restMarriageHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        MarriageHistory testMarriageHistory = marriageHistoryList.get(marriageHistoryList.size() - 1);
        assertThat(testMarriageHistory.getMarriageDate()).isEqualTo(DEFAULT_MARRIAGE_DATE);
        assertThat(testMarriageHistory.getParishMarriedAt()).isEqualTo(DEFAULT_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void createMarriageHistoryWithExistingId() throws Exception {
        // Create the MarriageHistory with an existing ID
        marriageHistory.setId(1L);
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        int databaseSizeBeforeCreate = marriageHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarriageHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMarriageDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageHistoryRepository.findAll().size();
        // set the field null
        marriageHistory.setMarriageDate(null);

        // Create the MarriageHistory, which fails.
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        restMarriageHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParishMarriedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageHistoryRepository.findAll().size();
        // set the field null
        marriageHistory.setParishMarriedAt(null);

        // Create the MarriageHistory, which fails.
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        restMarriageHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarriageHistories() throws Exception {
        // Initialize the database
        marriageHistoryRepository.saveAndFlush(marriageHistory);

        // Get all the marriageHistoryList
        restMarriageHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marriageHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].marriageDate").value(hasItem(DEFAULT_MARRIAGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].parishMarriedAt").value(hasItem(DEFAULT_PARISH_MARRIED_AT)));
    }

    @Test
    @Transactional
    void getMarriageHistory() throws Exception {
        // Initialize the database
        marriageHistoryRepository.saveAndFlush(marriageHistory);

        // Get the marriageHistory
        restMarriageHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, marriageHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marriageHistory.getId().intValue()))
            .andExpect(jsonPath("$.marriageDate").value(DEFAULT_MARRIAGE_DATE.toString()))
            .andExpect(jsonPath("$.parishMarriedAt").value(DEFAULT_PARISH_MARRIED_AT));
    }

    @Test
    @Transactional
    void getNonExistingMarriageHistory() throws Exception {
        // Get the marriageHistory
        restMarriageHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarriageHistory() throws Exception {
        // Initialize the database
        marriageHistoryRepository.saveAndFlush(marriageHistory);

        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();

        // Update the marriageHistory
        MarriageHistory updatedMarriageHistory = marriageHistoryRepository.findById(marriageHistory.getId()).get();
        // Disconnect from session so that the updates on updatedMarriageHistory are not directly saved in db
        em.detach(updatedMarriageHistory);
        updatedMarriageHistory.marriageDate(UPDATED_MARRIAGE_DATE).parishMarriedAt(UPDATED_PARISH_MARRIED_AT);
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(updatedMarriageHistory);

        restMarriageHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marriageHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
        MarriageHistory testMarriageHistory = marriageHistoryList.get(marriageHistoryList.size() - 1);
        assertThat(testMarriageHistory.getMarriageDate()).isEqualTo(UPDATED_MARRIAGE_DATE);
        assertThat(testMarriageHistory.getParishMarriedAt()).isEqualTo(UPDATED_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void putNonExistingMarriageHistory() throws Exception {
        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();
        marriageHistory.setId(count.incrementAndGet());

        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarriageHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marriageHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarriageHistory() throws Exception {
        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();
        marriageHistory.setId(count.incrementAndGet());

        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarriageHistory() throws Exception {
        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();
        marriageHistory.setId(count.incrementAndGet());

        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarriageHistoryWithPatch() throws Exception {
        // Initialize the database
        marriageHistoryRepository.saveAndFlush(marriageHistory);

        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();

        // Update the marriageHistory using partial update
        MarriageHistory partialUpdatedMarriageHistory = new MarriageHistory();
        partialUpdatedMarriageHistory.setId(marriageHistory.getId());

        partialUpdatedMarriageHistory.marriageDate(UPDATED_MARRIAGE_DATE).parishMarriedAt(UPDATED_PARISH_MARRIED_AT);

        restMarriageHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarriageHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarriageHistory))
            )
            .andExpect(status().isOk());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
        MarriageHistory testMarriageHistory = marriageHistoryList.get(marriageHistoryList.size() - 1);
        assertThat(testMarriageHistory.getMarriageDate()).isEqualTo(UPDATED_MARRIAGE_DATE);
        assertThat(testMarriageHistory.getParishMarriedAt()).isEqualTo(UPDATED_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void fullUpdateMarriageHistoryWithPatch() throws Exception {
        // Initialize the database
        marriageHistoryRepository.saveAndFlush(marriageHistory);

        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();

        // Update the marriageHistory using partial update
        MarriageHistory partialUpdatedMarriageHistory = new MarriageHistory();
        partialUpdatedMarriageHistory.setId(marriageHistory.getId());

        partialUpdatedMarriageHistory.marriageDate(UPDATED_MARRIAGE_DATE).parishMarriedAt(UPDATED_PARISH_MARRIED_AT);

        restMarriageHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarriageHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarriageHistory))
            )
            .andExpect(status().isOk());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
        MarriageHistory testMarriageHistory = marriageHistoryList.get(marriageHistoryList.size() - 1);
        assertThat(testMarriageHistory.getMarriageDate()).isEqualTo(UPDATED_MARRIAGE_DATE);
        assertThat(testMarriageHistory.getParishMarriedAt()).isEqualTo(UPDATED_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingMarriageHistory() throws Exception {
        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();
        marriageHistory.setId(count.incrementAndGet());

        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarriageHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marriageHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarriageHistory() throws Exception {
        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();
        marriageHistory.setId(count.incrementAndGet());

        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarriageHistory() throws Exception {
        int databaseSizeBeforeUpdate = marriageHistoryRepository.findAll().size();
        marriageHistory.setId(count.incrementAndGet());

        // Create the MarriageHistory
        MarriageHistoryDTO marriageHistoryDTO = marriageHistoryMapper.toDto(marriageHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marriageHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarriageHistory in the database
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarriageHistory() throws Exception {
        // Initialize the database
        marriageHistoryRepository.saveAndFlush(marriageHistory);

        int databaseSizeBeforeDelete = marriageHistoryRepository.findAll().size();

        // Delete the marriageHistory
        restMarriageHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, marriageHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MarriageHistory> marriageHistoryList = marriageHistoryRepository.findAll();
        assertThat(marriageHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
