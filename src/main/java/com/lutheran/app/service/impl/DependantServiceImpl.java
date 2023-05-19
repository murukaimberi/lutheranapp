package com.lutheran.app.service.impl;

import com.lutheran.app.domain.Dependant;
import com.lutheran.app.repository.DependantRepository;
import com.lutheran.app.service.DependantService;
import com.lutheran.app.service.dto.DependantDTO;
import com.lutheran.app.service.mapper.DependantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dependant}.
 */
@Service
@Transactional
public class DependantServiceImpl implements DependantService {

    private final Logger log = LoggerFactory.getLogger(DependantServiceImpl.class);

    private final DependantRepository dependantRepository;

    private final DependantMapper dependantMapper;

    public DependantServiceImpl(DependantRepository dependantRepository, DependantMapper dependantMapper) {
        this.dependantRepository = dependantRepository;
        this.dependantMapper = dependantMapper;
    }

    @Override
    public DependantDTO save(DependantDTO dependantDTO) {
        log.debug("Request to save Dependant : {}", dependantDTO);
        Dependant dependant = dependantMapper.toEntity(dependantDTO);
        dependant = dependantRepository.save(dependant);
        return dependantMapper.toDto(dependant);
    }

    @Override
    public DependantDTO update(DependantDTO dependantDTO) {
        log.debug("Request to update Dependant : {}", dependantDTO);
        Dependant dependant = dependantMapper.toEntity(dependantDTO);
        dependant = dependantRepository.save(dependant);
        return dependantMapper.toDto(dependant);
    }

    @Override
    public Optional<DependantDTO> partialUpdate(DependantDTO dependantDTO) {
        log.debug("Request to partially update Dependant : {}", dependantDTO);

        return dependantRepository
            .findById(dependantDTO.getId())
            .map(existingDependant -> {
                dependantMapper.partialUpdate(existingDependant, dependantDTO);

                return existingDependant;
            })
            .map(dependantRepository::save)
            .map(dependantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DependantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dependants");
        return dependantRepository.findAll(pageable).map(dependantMapper::toDto);
    }

    public Page<DependantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dependantRepository.findAllWithEagerRelationships(pageable).map(dependantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DependantDTO> findOne(Long id) {
        log.debug("Request to get Dependant : {}", id);
        return dependantRepository.findOneWithEagerRelationships(id).map(dependantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dependant : {}", id);
        dependantRepository.deleteById(id);
    }
}
