package com.lutheran.app.service;

import com.lutheran.app.service.dto.ContributionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lutheran.app.domain.Contribution}.
 */
public interface ContributionService {
    /**
     * Save a contribution.
     *
     * @param contributionDTO the entity to save.
     * @return the persisted entity.
     */
    ContributionDTO save(ContributionDTO contributionDTO);

    /**
     * Updates a contribution.
     *
     * @param contributionDTO the entity to update.
     * @return the persisted entity.
     */
    ContributionDTO update(ContributionDTO contributionDTO);

    /**
     * Partially updates a contribution.
     *
     * @param contributionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContributionDTO> partialUpdate(ContributionDTO contributionDTO);

    /**
     * Get all the contributions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContributionDTO> findAll(Pageable pageable);

    /**
     * Get all the contributions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContributionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contribution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContributionDTO> findOne(Long id);

    /**
     * Delete the "id" contribution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
