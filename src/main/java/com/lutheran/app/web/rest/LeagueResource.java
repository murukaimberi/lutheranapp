package com.lutheran.app.web.rest;

import com.lutheran.app.repository.LeagueRepository;
import com.lutheran.app.service.LeagueService;
import com.lutheran.app.service.dto.LeagueDTO;
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
 * REST controller for managing {@link com.lutheran.app.domain.League}.
 */
@RestController
@RequestMapping("/api")
public class LeagueResource {

    private final Logger log = LoggerFactory.getLogger(LeagueResource.class);

    private static final String ENTITY_NAME = "league";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeagueService leagueService;

    private final LeagueRepository leagueRepository;

    public LeagueResource(LeagueService leagueService, LeagueRepository leagueRepository) {
        this.leagueService = leagueService;
        this.leagueRepository = leagueRepository;
    }

    /**
     * {@code POST  /leagues} : Create a new league.
     *
     * @param leagueDTO the leagueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leagueDTO, or with status {@code 400 (Bad Request)} if the league has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leagues")
    public ResponseEntity<LeagueDTO> createLeague(@Valid @RequestBody LeagueDTO leagueDTO) throws URISyntaxException {
        log.debug("REST request to save League : {}", leagueDTO);
        if (leagueDTO.getId() != null) {
            throw new BadRequestAlertException("A new league cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeagueDTO result = leagueService.save(leagueDTO);
        return ResponseEntity
            .created(new URI("/api/leagues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leagues/:id} : Updates an existing league.
     *
     * @param id the id of the leagueDTO to save.
     * @param leagueDTO the leagueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leagueDTO,
     * or with status {@code 400 (Bad Request)} if the leagueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leagueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leagues/{id}")
    public ResponseEntity<LeagueDTO> updateLeague(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeagueDTO leagueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update League : {}, {}", id, leagueDTO);
        if (leagueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leagueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leagueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeagueDTO result = leagueService.update(leagueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leagueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leagues/:id} : Partial updates given fields of an existing league, field will ignore if it is null
     *
     * @param id the id of the leagueDTO to save.
     * @param leagueDTO the leagueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leagueDTO,
     * or with status {@code 400 (Bad Request)} if the leagueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leagueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leagueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leagues/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeagueDTO> partialUpdateLeague(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeagueDTO leagueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update League partially : {}, {}", id, leagueDTO);
        if (leagueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leagueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leagueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeagueDTO> result = leagueService.partialUpdate(leagueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leagueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /leagues} : get all the leagues.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leagues in body.
     */
    @GetMapping("/leagues")
    public ResponseEntity<List<LeagueDTO>> getAllLeagues(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Leagues");
        Page<LeagueDTO> page;
        if (eagerload) {
            page = leagueService.findAllWithEagerRelationships(pageable);
        } else {
            page = leagueService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leagues/:id} : get the "id" league.
     *
     * @param id the id of the leagueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leagueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leagues/{id}")
    public ResponseEntity<LeagueDTO> getLeague(@PathVariable Long id) {
        log.debug("REST request to get League : {}", id);
        Optional<LeagueDTO> leagueDTO = leagueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leagueDTO);
    }

    /**
     * {@code DELETE  /leagues/:id} : delete the "id" league.
     *
     * @param id the id of the leagueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leagues/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        log.debug("REST request to delete League : {}", id);
        leagueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
