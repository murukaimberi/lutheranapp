package com.lutheran.app.web.rest;

import com.lutheran.app.repository.MarriageHistoryRepository;
import com.lutheran.app.service.MarriageHistoryService;
import com.lutheran.app.service.dto.MarriageHistoryDTO;
import com.lutheran.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.lutheran.app.domain.MarriageHistory}.
 */
@RestController
@RequestMapping("/api")
public class MarriageHistoryResource {

    private final Logger log = LoggerFactory.getLogger(MarriageHistoryResource.class);

    private static final String ENTITY_NAME = "marriageHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarriageHistoryService marriageHistoryService;

    private final MarriageHistoryRepository marriageHistoryRepository;

    public MarriageHistoryResource(MarriageHistoryService marriageHistoryService, MarriageHistoryRepository marriageHistoryRepository) {
        this.marriageHistoryService = marriageHistoryService;
        this.marriageHistoryRepository = marriageHistoryRepository;
    }

    /**
     * {@code POST  /marriage-histories} : Create a new marriageHistory.
     *
     * @param marriageHistoryDTO the marriageHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marriageHistoryDTO, or with status {@code 400 (Bad Request)} if the marriageHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marriage-histories")
    public ResponseEntity<MarriageHistoryDTO> createMarriageHistory(@Valid @RequestBody MarriageHistoryDTO marriageHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save MarriageHistory : {}", marriageHistoryDTO);
        if (marriageHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new marriageHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarriageHistoryDTO result = marriageHistoryService.save(marriageHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/marriage-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marriage-histories/:id} : Updates an existing marriageHistory.
     *
     * @param id the id of the marriageHistoryDTO to save.
     * @param marriageHistoryDTO the marriageHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marriageHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the marriageHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marriageHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marriage-histories/{id}")
    public ResponseEntity<MarriageHistoryDTO> updateMarriageHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarriageHistoryDTO marriageHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MarriageHistory : {}, {}", id, marriageHistoryDTO);
        if (marriageHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marriageHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marriageHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarriageHistoryDTO result = marriageHistoryService.update(marriageHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marriageHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /marriage-histories/:id} : Partial updates given fields of an existing marriageHistory, field will ignore if it is null
     *
     * @param id the id of the marriageHistoryDTO to save.
     * @param marriageHistoryDTO the marriageHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marriageHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the marriageHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the marriageHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the marriageHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marriage-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarriageHistoryDTO> partialUpdateMarriageHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarriageHistoryDTO marriageHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarriageHistory partially : {}, {}", id, marriageHistoryDTO);
        if (marriageHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marriageHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marriageHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarriageHistoryDTO> result = marriageHistoryService.partialUpdate(marriageHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marriageHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /marriage-histories} : get all the marriageHistories.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marriageHistories in body.
     */
    @GetMapping("/marriage-histories")
    public ResponseEntity<List<MarriageHistoryDTO>> getAllMarriageHistories(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("congregant-is-null".equals(filter)) {
            log.debug("REST request to get all MarriageHistorys where congregant is null");
            return new ResponseEntity<>(marriageHistoryService.findAllWhereCongregantIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of MarriageHistories");
        Page<MarriageHistoryDTO> page = marriageHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marriage-histories/:id} : get the "id" marriageHistory.
     *
     * @param id the id of the marriageHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marriageHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marriage-histories/{id}")
    public ResponseEntity<MarriageHistoryDTO> getMarriageHistory(@PathVariable Long id) {
        log.debug("REST request to get MarriageHistory : {}", id);
        Optional<MarriageHistoryDTO> marriageHistoryDTO = marriageHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marriageHistoryDTO);
    }

    /**
     * {@code DELETE  /marriage-histories/:id} : delete the "id" marriageHistory.
     *
     * @param id the id of the marriageHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marriage-histories/{id}")
    public ResponseEntity<Void> deleteMarriageHistory(@PathVariable Long id) {
        log.debug("REST request to delete MarriageHistory : {}", id);
        marriageHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
