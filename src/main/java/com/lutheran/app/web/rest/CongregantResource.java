package com.lutheran.app.web.rest;

import com.lutheran.app.repository.CongregantRepository;
import com.lutheran.app.service.CongregantService;
import com.lutheran.app.service.dto.CongregantDTO;
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
 * REST controller for managing {@link com.lutheran.app.domain.Congregant}.
 */
@RestController
@RequestMapping("/api")
public class CongregantResource {

    private final Logger log = LoggerFactory.getLogger(CongregantResource.class);

    private static final String ENTITY_NAME = "congregant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CongregantService congregantService;

    private final CongregantRepository congregantRepository;

    public CongregantResource(CongregantService congregantService, CongregantRepository congregantRepository) {
        this.congregantService = congregantService;
        this.congregantRepository = congregantRepository;
    }

    /**
     * {@code POST  /congregants} : Create a new congregant.
     *
     * @param congregantDTO the congregantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new congregantDTO, or with status {@code 400 (Bad Request)} if the congregant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/congregants")
    public ResponseEntity<CongregantDTO> createCongregant(@Valid @RequestBody CongregantDTO congregantDTO) throws URISyntaxException {
        log.debug("REST request to save Congregant : {}", congregantDTO);
        if (congregantDTO.getId() != null) {
            throw new BadRequestAlertException("A new congregant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CongregantDTO result = congregantService.save(congregantDTO);
        return ResponseEntity
            .created(new URI("/api/congregants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /congregants/:id} : Updates an existing congregant.
     *
     * @param id the id of the congregantDTO to save.
     * @param congregantDTO the congregantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated congregantDTO,
     * or with status {@code 400 (Bad Request)} if the congregantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the congregantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/congregants/{id}")
    public ResponseEntity<CongregantDTO> updateCongregant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CongregantDTO congregantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Congregant : {}, {}", id, congregantDTO);
        if (congregantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, congregantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!congregantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CongregantDTO result = congregantService.update(congregantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, congregantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /congregants/:id} : Partial updates given fields of an existing congregant, field will ignore if it is null
     *
     * @param id the id of the congregantDTO to save.
     * @param congregantDTO the congregantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated congregantDTO,
     * or with status {@code 400 (Bad Request)} if the congregantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the congregantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the congregantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/congregants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CongregantDTO> partialUpdateCongregant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CongregantDTO congregantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Congregant partially : {}, {}", id, congregantDTO);
        if (congregantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, congregantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!congregantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CongregantDTO> result = congregantService.partialUpdate(congregantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, congregantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /congregants} : get all the congregants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of congregants in body.
     */
    @GetMapping("/congregants")
    public ResponseEntity<List<CongregantDTO>> getAllCongregants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Congregants");
        Page<CongregantDTO> page;
        if (eagerload) {
            page = congregantService.findAllWithEagerRelationships(pageable);
        } else {
            page = congregantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /congregants/:id} : get the "id" congregant.
     *
     * @param id the id of the congregantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the congregantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/congregants/{id}")
    public ResponseEntity<CongregantDTO> getCongregant(@PathVariable Long id) {
        log.debug("REST request to get Congregant : {}", id);
        Optional<CongregantDTO> congregantDTO = congregantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(congregantDTO);
    }

    /**
     * {@code DELETE  /congregants/:id} : delete the "id" congregant.
     *
     * @param id the id of the congregantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/congregants/{id}")
    public ResponseEntity<Void> deleteCongregant(@PathVariable Long id) {
        log.debug("REST request to delete Congregant : {}", id);
        congregantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
