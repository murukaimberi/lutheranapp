package com.lutheran.app.service;

import com.lutheran.app.service.dto.CongregantDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lutheran.app.domain.Congregant}.
 */
public interface CongregantService {
    /**
     * Save a congregant.
     *
     * @param congregantDTO the entity to save.
     * @return the persisted entity.
     */
    CongregantDTO save(CongregantDTO congregantDTO);

    /**
     * Updates a congregant.
     *
     * @param congregantDTO the entity to update.
     * @return the persisted entity.
     */
    CongregantDTO update(CongregantDTO congregantDTO);

    /**
     * Partially updates a congregant.
     *
     * @param congregantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CongregantDTO> partialUpdate(CongregantDTO congregantDTO);

    /**
     * Get all the congregants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CongregantDTO> findAll(Pageable pageable);

    /**
     * Get all the congregants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CongregantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" congregant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CongregantDTO> findOne(Long id);

    /**
     * Delete the "id" congregant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
