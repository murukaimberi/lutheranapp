package com.lutheran.app.web.rest;

import com.lutheran.app.repository.ContributionRepository;
import com.lutheran.app.service.ContributionService;
import com.lutheran.app.service.dto.ContributionDTO;
import com.lutheran.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lutheran.app.domain.Contribution}.
 */
@RestController
@RequestMapping("/api")
public class ContributionResource {

    private final Logger log = LoggerFactory.getLogger(ContributionResource.class);

    private static final String ENTITY_NAME = "contribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContributionService contributionService;

    private final ContributionRepository contributionRepository;

    public ContributionResource(ContributionService contributionService, ContributionRepository contributionRepository) {
        this.contributionService = contributionService;
        this.contributionRepository = contributionRepository;
    }

    /**
     * {@code POST  /contributions} : Create a new contribution.
     *
     * @param contributionDTO the contributionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contributionDTO, or with status {@code 400 (Bad Request)} if the contribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contributions")
    public ResponseEntity<ContributionDTO> createContribution(@Valid @RequestBody ContributionDTO contributionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Contribution : {}", contributionDTO);
        if (contributionDTO.getId() != null) {
            throw new BadRequestAlertException("A new contribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionDTO result = contributionService.save(contributionDTO);
        return ResponseEntity
            .created(new URI("/api/contributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contributions/:id} : Updates an existing contribution.
     *
     * @param id the id of the contributionDTO to save.
     * @param contributionDTO the contributionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributionDTO,
     * or with status {@code 400 (Bad Request)} if the contributionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contributionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contributions/{id}")
    public ResponseEntity<ContributionDTO> updateContribution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContributionDTO contributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contribution : {}, {}", id, contributionDTO);
        if (contributionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contributionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContributionDTO result = contributionService.update(contributionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contributionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contributions/:id} : Partial updates given fields of an existing contribution, field will ignore if it is null
     *
     * @param id the id of the contributionDTO to save.
     * @param contributionDTO the contributionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributionDTO,
     * or with status {@code 400 (Bad Request)} if the contributionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contributionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contributionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contributions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContributionDTO> partialUpdateContribution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContributionDTO contributionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contribution partially : {}, {}", id, contributionDTO);
        if (contributionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contributionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContributionDTO> result = contributionService.partialUpdate(contributionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contributionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contributions} : get all the contributions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contributions in body.
     */
    @GetMapping("/contributions")
    public ResponseEntity<List<ContributionDTO>> getAllContributions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Contributions");
        Page<ContributionDTO> page;
        if (eagerload) {
            page = contributionService.findAllWithEagerRelationships(pageable);
        } else {
            page = contributionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contributions/:id} : get the "id" contribution.
     *
     * @param id the id of the contributionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contributionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contributions/{id}")
    public ResponseEntity<ContributionDTO> getContribution(@PathVariable Long id) {
        log.debug("REST request to get Contribution : {}", id);
        Optional<ContributionDTO> contributionDTO = contributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contributionDTO);
    }

    /**
     * {@code DELETE  /contributions/:id} : delete the "id" contribution.
     *
     * @param id the id of the contributionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contributions/{id}")
    public ResponseEntity<Void> deleteContribution(@PathVariable Long id) {
        log.debug("REST request to delete Contribution : {}", id);
        contributionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
