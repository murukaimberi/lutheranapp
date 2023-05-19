package com.lutheran.app.service;

import com.lutheran.app.service.dto.MarriageHistoryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.lutheran.app.domain.MarriageHistory}.
 */
public interface MarriageHistoryService {
    /**
     * Save a marriageHistory.
     *
     * @param marriageHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    MarriageHistoryDTO save(MarriageHistoryDTO marriageHistoryDTO);

    /**
     * Updates a marriageHistory.
     *
     * @param marriageHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    MarriageHistoryDTO update(MarriageHistoryDTO marriageHistoryDTO);

    /**
     * Partially updates a marriageHistory.
     *
     * @param marriageHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MarriageHistoryDTO> partialUpdate(MarriageHistoryDTO marriageHistoryDTO);

    /**
     * Get all the marriageHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MarriageHistoryDTO> findAll(Pageable pageable);
    /**
     * Get all the MarriageHistoryDTO where Congregant is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<MarriageHistoryDTO> findAllWhereCongregantIsNull();

    /**
     * Get the "id" marriageHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MarriageHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" marriageHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
