package com.lutheran.app.service;

import com.lutheran.app.service.dto.BaptismHistoryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lutheran.app.domain.BaptismHistory}.
 */
public interface BaptismHistoryService {
    /**
     * Save a baptismHistory.
     *
     * @param baptismHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    BaptismHistoryDTO save(BaptismHistoryDTO baptismHistoryDTO);

    /**
     * Updates a baptismHistory.
     *
     * @param baptismHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    BaptismHistoryDTO update(BaptismHistoryDTO baptismHistoryDTO);

    /**
     * Partially updates a baptismHistory.
     *
     * @param baptismHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BaptismHistoryDTO> partialUpdate(BaptismHistoryDTO baptismHistoryDTO);

    /**
     * Get all the baptismHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaptismHistoryDTO> findAll(Pageable pageable);
    /**
     * Get all the BaptismHistoryDTO where Congragant is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<BaptismHistoryDTO> findAllWhereCongragantIsNull();

    /**
     * Get the "id" baptismHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaptismHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" baptismHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
