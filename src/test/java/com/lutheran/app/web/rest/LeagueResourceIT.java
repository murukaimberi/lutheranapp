package com.lutheran.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lutheran.app.IntegrationTest;
import com.lutheran.app.domain.League;
import com.lutheran.app.repository.LeagueRepository;
import com.lutheran.app.service.LeagueService;
import com.lutheran.app.service.dto.LeagueDTO;
import com.lutheran.app.service.mapper.LeagueMapper;
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
 * Integration tests for the {@link LeagueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LeagueResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/leagues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeagueRepository leagueRepository;

    @Mock
    private LeagueRepository leagueRepositoryMock;

    @Autowired
    private LeagueMapper leagueMapper;

    @Mock
    private LeagueService leagueServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeagueMockMvc;

    private League league;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static League createEntity(EntityManager em) {
        League league = new League().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).createdDate(DEFAULT_CREATED_DATE);
        return league;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static League createUpdatedEntity(EntityManager em) {
        League league = new League().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);
        return league;
    }

    @BeforeEach
    public void initTest() {
        league = createEntity(em);
    }

    @Test
    @Transactional
    void createLeague() throws Exception {
        int databaseSizeBeforeCreate = leagueRepository.findAll().size();
        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);
        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isCreated());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeCreate + 1);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLeague.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeague.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createLeagueWithExistingId() throws Exception {
        // Create the League with an existing ID
        league.setId(1L);
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        int databaseSizeBeforeCreate = leagueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueRepository.findAll().size();
        // set the field null
        league.setName(null);

        // Create the League, which fails.
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isBadRequest());

        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leagueRepository.findAll().size();
        // set the field null
        league.setCreatedDate(null);

        // Create the League, which fails.
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        restLeagueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isBadRequest());

        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeagues() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get all the leagueList
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(league.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaguesWithEagerRelationshipsIsEnabled() throws Exception {
        when(leagueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeagueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(leagueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLeaguesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(leagueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLeagueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(leagueRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        // Get the league
        restLeagueMockMvc
            .perform(get(ENTITY_API_URL_ID, league.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(league.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLeague() throws Exception {
        // Get the league
        restLeagueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league
        League updatedLeague = leagueRepository.findById(league.getId()).get();
        // Disconnect from session so that the updates on updatedLeague are not directly saved in db
        em.detach(updatedLeague);
        updatedLeague.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);
        LeagueDTO leagueDTO = leagueMapper.toDto(updatedLeague);

        restLeagueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leagueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLeague.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeague.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leagueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leagueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeagueWithPatch() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league using partial update
        League partialUpdatedLeague = new League();
        partialUpdatedLeague.setId(league.getId());

        partialUpdatedLeague.createdDate(UPDATED_CREATED_DATE);

        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeague.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeague))
            )
            .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLeague.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeague.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLeagueWithPatch() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();

        // Update the league using partial update
        League partialUpdatedLeague = new League();
        partialUpdatedLeague.setId(league.getId());

        partialUpdatedLeague.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdDate(UPDATED_CREATED_DATE);

        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeague.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeague))
            )
            .andExpect(status().isOk());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
        League testLeague = leagueList.get(leagueList.size() - 1);
        assertThat(testLeague.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLeague.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeague.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leagueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeague() throws Exception {
        int databaseSizeBeforeUpdate = leagueRepository.findAll().size();
        league.setId(count.incrementAndGet());

        // Create the League
        LeagueDTO leagueDTO = leagueMapper.toDto(league);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeagueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(leagueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the League in the database
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeague() throws Exception {
        // Initialize the database
        leagueRepository.saveAndFlush(league);

        int databaseSizeBeforeDelete = leagueRepository.findAll().size();

        // Delete the league
        restLeagueMockMvc
            .perform(delete(ENTITY_API_URL_ID, league.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<League> leagueList = leagueRepository.findAll();
        assertThat(leagueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
