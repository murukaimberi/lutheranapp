package com.lutheran.app.service.impl;

import com.lutheran.app.domain.Contribution;
import com.lutheran.app.repository.ContributionRepository;
import com.lutheran.app.service.ContributionService;
import com.lutheran.app.service.dto.ContributionDTO;
import com.lutheran.app.service.mapper.ContributionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contribution}.
 */
@Service
@Transactional
public class ContributionServiceImpl implements ContributionService {

    private final Logger log = LoggerFactory.getLogger(ContributionServiceImpl.class);

    private final ContributionRepository contributionRepository;

    private final ContributionMapper contributionMapper;

    public ContributionServiceImpl(ContributionRepository contributionRepository, ContributionMapper contributionMapper) {
        this.contributionRepository = contributionRepository;
        this.contributionMapper = contributionMapper;
    }

    @Override
    public ContributionDTO save(ContributionDTO contributionDTO) {
        log.debug("Request to save Contribution : {}", contributionDTO);
        Contribution contribution = contributionMapper.toEntity(contributionDTO);
        contribution = contributionRepository.save(contribution);
        return contributionMapper.toDto(contribution);
    }

    @Override
    public ContributionDTO update(ContributionDTO contributionDTO) {
        log.debug("Request to update Contribution : {}", contributionDTO);
        Contribution contribution = contributionMapper.toEntity(contributionDTO);
        contribution = contributionRepository.save(contribution);
        return contributionMapper.toDto(contribution);
    }

    @Override
    public Optional<ContributionDTO> partialUpdate(ContributionDTO contributionDTO) {
        log.debug("Request to partially update Contribution : {}", contributionDTO);

        return contributionRepository
            .findById(contributionDTO.getId())
            .map(existingContribution -> {
                contributionMapper.partialUpdate(existingContribution, contributionDTO);

                return existingContribution;
            })
            .map(contributionRepository::save)
            .map(contributionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContributionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contributions");
        return contributionRepository.findAll(pageable).map(contributionMapper::toDto);
    }

    public Page<ContributionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contributionRepository.findAllWithEagerRelationships(pageable).map(contributionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContributionDTO> findOne(Long id) {
        log.debug("Request to get Contribution : {}", id);
        return contributionRepository.findOneWithEagerRelationships(id).map(contributionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contribution : {}", id);
        contributionRepository.deleteById(id);
    }
}
