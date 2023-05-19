package com.lutheran.app.repository;

import com.lutheran.app.domain.Congregant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Congregant entity.
 */
@Repository
public interface CongregantRepository extends JpaRepository<Congregant, Long> {
    default Optional<Congregant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Congregant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Congregant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct congregant from Congregant congregant left join fetch congregant.user",
        countQuery = "select count(distinct congregant) from Congregant congregant"
    )
    Page<Congregant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct congregant from Congregant congregant left join fetch congregant.user")
    List<Congregant> findAllWithToOneRelationships();

    @Query("select congregant from Congregant congregant left join fetch congregant.user where congregant.id =:id")
    Optional<Congregant> findOneWithToOneRelationships(@Param("id") Long id);
}
