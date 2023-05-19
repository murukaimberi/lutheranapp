package com.lutheran.app.service;

import com.lutheran.app.service.dto.LeagueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lutheran.app.domain.League}.
 */
public interface LeagueService {
    /**
     * Save a league.
     *
     * @param leagueDTO the entity to save.
     * @return the persisted entity.
     */
    LeagueDTO save(LeagueDTO leagueDTO);

    /**
     * Updates a league.
     *
     * @param leagueDTO the entity to update.
     * @return the persisted entity.
     */
    LeagueDTO update(LeagueDTO leagueDTO);

    /**
     * Partially updates a league.
     *
     * @param leagueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeagueDTO> partialUpdate(LeagueDTO leagueDTO);

    /**
     * Get all the leagues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeagueDTO> findAll(Pageable pageable);

    /**
     * Get all the leagues with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeagueDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" league.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeagueDTO> findOne(Long id);

    /**
     * Delete the "id" league.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
