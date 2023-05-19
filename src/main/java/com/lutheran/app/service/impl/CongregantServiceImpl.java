package com.lutheran.app.service.impl;

import com.lutheran.app.domain.Congregant;
import com.lutheran.app.repository.CongregantRepository;
import com.lutheran.app.service.CongregantService;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.mapper.CongregantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Congregant}.
 */
@Service
@Transactional
public class CongregantServiceImpl implements CongregantService {

    private final Logger log = LoggerFactory.getLogger(CongregantServiceImpl.class);

    private final CongregantRepository congregantRepository;

    private final CongregantMapper congregantMapper;

    public CongregantServiceImpl(CongregantRepository congregantRepository, CongregantMapper congregantMapper) {
        this.congregantRepository = congregantRepository;
        this.congregantMapper = congregantMapper;
    }

    @Override
    public CongregantDTO save(CongregantDTO congregantDTO) {
        log.debug("Request to save Congregant : {}", congregantDTO);
        Congregant congregant = congregantMapper.toEntity(congregantDTO);
        congregant = congregantRepository.save(congregant);
        return congregantMapper.toDto(congregant);
    }

    @Override
    public CongregantDTO update(CongregantDTO congregantDTO) {
        log.debug("Request to update Congregant : {}", congregantDTO);
        Congregant congregant = congregantMapper.toEntity(congregantDTO);
        congregant = congregantRepository.save(congregant);
        return congregantMapper.toDto(congregant);
    }

    @Override
    public Optional<CongregantDTO> partialUpdate(CongregantDTO congregantDTO) {
        log.debug("Request to partially update Congregant : {}", congregantDTO);

        return congregantRepository
            .findById(congregantDTO.getId())
            .map(existingCongregant -> {
                congregantMapper.partialUpdate(existingCongregant, congregantDTO);

                return existingCongregant;
            })
            .map(congregantRepository::save)
            .map(congregantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CongregantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Congregants");
        return congregantRepository.findAll(pageable).map(congregantMapper::toDto);
    }

    public Page<CongregantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return congregantRepository.findAllWithEagerRelationships(pageable).map(congregantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CongregantDTO> findOne(Long id) {
        log.debug("Request to get Congregant : {}", id);
        return congregantRepository.findOneWithEagerRelationships(id).map(congregantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Congregant : {}", id);
        congregantRepository.deleteById(id);
    }
}
