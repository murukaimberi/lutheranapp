package com.lutheran.app.service;

import com.lutheran.app.service.dto.DependantDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lutheran.app.domain.Dependant}.
 */
public interface DependantService {
    /**
     * Save a dependant.
     *
     * @param dependantDTO the entity to save.
     * @return the persisted entity.
     */
    DependantDTO save(DependantDTO dependantDTO);

    /**
     * Updates a dependant.
     *
     * @param dependantDTO the entity to update.
     * @return the persisted entity.
     */
    DependantDTO update(DependantDTO dependantDTO);

    /**
     * Partially updates a dependant.
     *
     * @param dependantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DependantDTO> partialUpdate(DependantDTO dependantDTO);

    /**
     * Get all the dependants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DependantDTO> findAll(Pageable pageable);

    /**
     * Get all the dependants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DependantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dependant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DependantDTO> findOne(Long id);

    /**
     * Delete the "id" dependant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
