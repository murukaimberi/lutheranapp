package com.lutheran.app.repository;

import com.lutheran.app.domain.Contribution;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contribution entity.
 */
@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    default Optional<Contribution> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Contribution> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Contribution> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct contribution from Contribution contribution left join fetch contribution.congregant",
        countQuery = "select count(distinct contribution) from Contribution contribution"
    )
    Page<Contribution> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct contribution from Contribution contribution left join fetch contribution.congregant")
    List<Contribution> findAllWithToOneRelationships();

    @Query("select contribution from Contribution contribution left join fetch contribution.congregant where contribution.id =:id")
    Optional<Contribution> findOneWithToOneRelationships(@Param("id") Long id);
}
