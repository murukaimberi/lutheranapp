package com.lutheran.app.web.rest;

import static com.lutheran.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lutheran.app.IntegrationTest;
import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.Contribution;
import com.lutheran.app.domain.enumeration.ContributionType;
import com.lutheran.app.domain.enumeration.Frequency;
import com.lutheran.app.repository.ContributionRepository;
import com.lutheran.app.service.ContributionService;
import com.lutheran.app.service.dto.ContributionDTO;
import com.lutheran.app.service.mapper.ContributionMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ContributionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContributionResourceIT {

    private static final ContributionType DEFAULT_CONTRIBUTION_TYPE = ContributionType.REGISTRATION;
    private static final ContributionType UPDATED_CONTRIBUTION_TYPE = ContributionType.ANNUAL_DUES;

    private static final Frequency DEFAULT_FREQUENCY = Frequency.YEARLY;
    private static final Frequency UPDATED_FREQUENCY = Frequency.LUMP_SUM;

    private static final LocalDate DEFAULT_MONTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MONTH = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/contributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContributionRepository contributionRepository;

    @Mock
    private ContributionRepository contributionRepositoryMock;

    @Autowired
    private ContributionMapper contributionMapper;

    @Mock
    private ContributionService contributionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContributionMockMvc;

    private Contribution contribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contribution createEntity(EntityManager em) {
        Contribution contribution = new Contribution()
            .contributionType(DEFAULT_CONTRIBUTION_TYPE)
            .frequency(DEFAULT_FREQUENCY)
            .month(DEFAULT_MONTH)
            .amount(DEFAULT_AMOUNT);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        contribution.setCongregant(congregant);
        return contribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contribution createUpdatedEntity(EntityManager em) {
        Contribution contribution = new Contribution()
            .contributionType(UPDATED_CONTRIBUTION_TYPE)
            .frequency(UPDATED_FREQUENCY)
            .month(UPDATED_MONTH)
            .amount(UPDATED_AMOUNT);
        // Add required entity
        Congregant congregant;
        if (TestUtil.findAll(em, Congregant.class).isEmpty()) {
            congregant = CongregantResourceIT.createUpdatedEntity(em);
            em.persist(congregant);
            em.flush();
        } else {
            congregant = TestUtil.findAll(em, Congregant.class).get(0);
        }
        contribution.setCongregant(congregant);
        return contribution;
    }

    @BeforeEach
    public void initTest() {
        contribution = createEntity(em);
    }

    @Test
    @Transactional
    void createContribution() throws Exception {
        int databaseSizeBeforeCreate = contributionRepository.findAll().size();
        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);
        restContributionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeCreate + 1);
        Contribution testContribution = contributionList.get(contributionList.size() - 1);
        assertThat(testContribution.getContributionType()).isEqualTo(DEFAULT_CONTRIBUTION_TYPE);
        assertThat(testContribution.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testContribution.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testContribution.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void createContributionWithExistingId() throws Exception {
        // Create the Contribution with an existing ID
        contribution.setId(1L);
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        int databaseSizeBeforeCreate = contributionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContributionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributionRepository.findAll().size();
        // set the field null
        contribution.setContributionType(null);

        // Create the Contribution, which fails.
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        restContributionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributionRepository.findAll().size();
        // set the field null
        contribution.setFrequency(null);

        // Create the Contribution, which fails.
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        restContributionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributionRepository.findAll().size();
        // set the field null
        contribution.setMonth(null);

        // Create the Contribution, which fails.
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        restContributionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributionRepository.findAll().size();
        // set the field null
        contribution.setAmount(null);

        // Create the Contribution, which fails.
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        restContributionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContributions() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get all the contributionList
        restContributionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].contributionType").value(hasItem(DEFAULT_CONTRIBUTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContributionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contributionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContributionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contributionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContributionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contributionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContributionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contributionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get the contribution
        restContributionMockMvc
            .perform(get(ENTITY_API_URL_ID, contribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contribution.getId().intValue()))
            .andExpect(jsonPath("$.contributionType").value(DEFAULT_CONTRIBUTION_TYPE.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingContribution() throws Exception {
        // Get the contribution
        restContributionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();

        // Update the contribution
        Contribution updatedContribution = contributionRepository.findById(contribution.getId()).get();
        // Disconnect from session so that the updates on updatedContribution are not directly saved in db
        em.detach(updatedContribution);
        updatedContribution
            .contributionType(UPDATED_CONTRIBUTION_TYPE)
            .frequency(UPDATED_FREQUENCY)
            .month(UPDATED_MONTH)
            .amount(UPDATED_AMOUNT);
        ContributionDTO contributionDTO = contributionMapper.toDto(updatedContribution);

        restContributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contributionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
        Contribution testContribution = contributionList.get(contributionList.size() - 1);
        assertThat(testContribution.getContributionType()).isEqualTo(UPDATED_CONTRIBUTION_TYPE);
        assertThat(testContribution.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testContribution.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testContribution.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();
        contribution.setId(count.incrementAndGet());

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contributionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();
        contribution.setId(count.incrementAndGet());

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();
        contribution.setId(count.incrementAndGet());

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContributionWithPatch() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();

        // Update the contribution using partial update
        Contribution partialUpdatedContribution = new Contribution();
        partialUpdatedContribution.setId(contribution.getId());

        partialUpdatedContribution.frequency(UPDATED_FREQUENCY).amount(UPDATED_AMOUNT);

        restContributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContribution))
            )
            .andExpect(status().isOk());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
        Contribution testContribution = contributionList.get(contributionList.size() - 1);
        assertThat(testContribution.getContributionType()).isEqualTo(DEFAULT_CONTRIBUTION_TYPE);
        assertThat(testContribution.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testContribution.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testContribution.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateContributionWithPatch() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();

        // Update the contribution using partial update
        Contribution partialUpdatedContribution = new Contribution();
        partialUpdatedContribution.setId(contribution.getId());

        partialUpdatedContribution
            .contributionType(UPDATED_CONTRIBUTION_TYPE)
            .frequency(UPDATED_FREQUENCY)
            .month(UPDATED_MONTH)
            .amount(UPDATED_AMOUNT);

        restContributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContribution))
            )
            .andExpect(status().isOk());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
        Contribution testContribution = contributionList.get(contributionList.size() - 1);
        assertThat(testContribution.getContributionType()).isEqualTo(UPDATED_CONTRIBUTION_TYPE);
        assertThat(testContribution.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testContribution.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testContribution.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();
        contribution.setId(count.incrementAndGet());

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contributionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();
        contribution.setId(count.incrementAndGet());

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContribution() throws Exception {
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();
        contribution.setId(count.incrementAndGet());

        // Create the Contribution
        ContributionDTO contributionDTO = contributionMapper.toDto(contribution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contribution in the database
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        int databaseSizeBeforeDelete = contributionRepository.findAll().size();

        // Delete the contribution
        restContributionMockMvc
            .perform(delete(ENTITY_API_URL_ID, contribution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contribution> contributionList = contributionRepository.findAll();
        assertThat(contributionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
