package com.lutheran.app.service.impl;

import com.lutheran.app.domain.MarriageHistory;
import com.lutheran.app.repository.MarriageHistoryRepository;
import com.lutheran.app.service.MarriageHistoryService;
import com.lutheran.app.service.dto.MarriageHistoryDTO;
import com.lutheran.app.service.mapper.MarriageHistoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MarriageHistory}.
 */
@Service
@Transactional
public class MarriageHistoryServiceImpl implements MarriageHistoryService {

    private final Logger log = LoggerFactory.getLogger(MarriageHistoryServiceImpl.class);

    private final MarriageHistoryRepository marriageHistoryRepository;

    private final MarriageHistoryMapper marriageHistoryMapper;

    public MarriageHistoryServiceImpl(MarriageHistoryRepository marriageHistoryRepository, MarriageHistoryMapper marriageHistoryMapper) {
        this.marriageHistoryRepository = marriageHistoryRepository;
        this.marriageHistoryMapper = marriageHistoryMapper;
    }

    @Override
    public MarriageHistoryDTO save(MarriageHistoryDTO marriageHistoryDTO) {
        log.debug("Request to save MarriageHistory : {}", marriageHistoryDTO);
        MarriageHistory marriageHistory = marriageHistoryMapper.toEntity(marriageHistoryDTO);
        marriageHistory = marriageHistoryRepository.save(marriageHistory);
        return marriageHistoryMapper.toDto(marriageHistory);
    }

    @Override
    public MarriageHistoryDTO update(MarriageHistoryDTO marriageHistoryDTO) {
        log.debug("Request to update MarriageHistory : {}", marriageHistoryDTO);
        MarriageHistory marriageHistory = marriageHistoryMapper.toEntity(marriageHistoryDTO);
        marriageHistory = marriageHistoryRepository.save(marriageHistory);
        return marriageHistoryMapper.toDto(marriageHistory);
    }

    @Override
    public Optional<MarriageHistoryDTO> partialUpdate(MarriageHistoryDTO marriageHistoryDTO) {
        log.debug("Request to partially update MarriageHistory : {}", marriageHistoryDTO);

        return marriageHistoryRepository
            .findById(marriageHistoryDTO.getId())
            .map(existingMarriageHistory -> {
                marriageHistoryMapper.partialUpdate(existingMarriageHistory, marriageHistoryDTO);

                return existingMarriageHistory;
            })
            .map(marriageHistoryRepository::save)
            .map(marriageHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarriageHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MarriageHistories");
        return marriageHistoryRepository.findAll(pageable).map(marriageHistoryMapper::toDto);
    }

    /**
     *  Get all the marriageHistories where Congregant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MarriageHistoryDTO> findAllWhereCongregantIsNull() {
        log.debug("Request to get all marriageHistories where Congregant is null");
        return StreamSupport
            .stream(marriageHistoryRepository.findAll().spliterator(), false)
            .filter(marriageHistory -> marriageHistory.getCongregant() == null)
            .map(marriageHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarriageHistoryDTO> findOne(Long id) {
        log.debug("Request to get MarriageHistory : {}", id);
        return marriageHistoryRepository.findById(id).map(marriageHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MarriageHistory : {}", id);
        marriageHistoryRepository.deleteById(id);
    }
}
