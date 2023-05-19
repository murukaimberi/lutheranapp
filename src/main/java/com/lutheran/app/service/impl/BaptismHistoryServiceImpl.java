package com.lutheran.app.service.impl;

import com.lutheran.app.domain.BaptismHistory;
import com.lutheran.app.repository.BaptismHistoryRepository;
import com.lutheran.app.service.BaptismHistoryService;
import com.lutheran.app.service.dto.BaptismHistoryDTO;
import com.lutheran.app.service.mapper.BaptismHistoryMapper;
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
 * Service Implementation for managing {@link BaptismHistory}.
 */
@Service
@Transactional
public class BaptismHistoryServiceImpl implements BaptismHistoryService {

    private final Logger log = LoggerFactory.getLogger(BaptismHistoryServiceImpl.class);

    private final BaptismHistoryRepository baptismHistoryRepository;

    private final BaptismHistoryMapper baptismHistoryMapper;

    public BaptismHistoryServiceImpl(BaptismHistoryRepository baptismHistoryRepository, BaptismHistoryMapper baptismHistoryMapper) {
        this.baptismHistoryRepository = baptismHistoryRepository;
        this.baptismHistoryMapper = baptismHistoryMapper;
    }

    @Override
    public BaptismHistoryDTO save(BaptismHistoryDTO baptismHistoryDTO) {
        log.debug("Request to save BaptismHistory : {}", baptismHistoryDTO);
        BaptismHistory baptismHistory = baptismHistoryMapper.toEntity(baptismHistoryDTO);
        baptismHistory = baptismHistoryRepository.save(baptismHistory);
        return baptismHistoryMapper.toDto(baptismHistory);
    }

    @Override
    public BaptismHistoryDTO update(BaptismHistoryDTO baptismHistoryDTO) {
        log.debug("Request to update BaptismHistory : {}", baptismHistoryDTO);
        BaptismHistory baptismHistory = baptismHistoryMapper.toEntity(baptismHistoryDTO);
        baptismHistory = baptismHistoryRepository.save(baptismHistory);
        return baptismHistoryMapper.toDto(baptismHistory);
    }

    @Override
    public Optional<BaptismHistoryDTO> partialUpdate(BaptismHistoryDTO baptismHistoryDTO) {
        log.debug("Request to partially update BaptismHistory : {}", baptismHistoryDTO);

        return baptismHistoryRepository
            .findById(baptismHistoryDTO.getId())
            .map(existingBaptismHistory -> {
                baptismHistoryMapper.partialUpdate(existingBaptismHistory, baptismHistoryDTO);

                return existingBaptismHistory;
            })
            .map(baptismHistoryRepository::save)
            .map(baptismHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BaptismHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BaptismHistories");
        return baptismHistoryRepository.findAll(pageable).map(baptismHistoryMapper::toDto);
    }

    /**
     *  Get all the baptismHistories where Congragant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BaptismHistoryDTO> findAllWhereCongragantIsNull() {
        log.debug("Request to get all baptismHistories where Congragant is null");
        return StreamSupport
            .stream(baptismHistoryRepository.findAll().spliterator(), false)
            .filter(baptismHistory -> baptismHistory.getCongragant() == null)
            .map(baptismHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BaptismHistoryDTO> findOne(Long id) {
        log.debug("Request to get BaptismHistory : {}", id);
        return baptismHistoryRepository.findById(id).map(baptismHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BaptismHistory : {}", id);
        baptismHistoryRepository.deleteById(id);
    }
}
