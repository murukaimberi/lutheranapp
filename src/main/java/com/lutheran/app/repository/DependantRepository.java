package com.lutheran.app.repository;

import com.lutheran.app.domain.Dependant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dependant entity.
 */
@Repository
public interface DependantRepository extends JpaRepository<Dependant, Long> {
    default Optional<Dependant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Dependant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Dependant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct dependant from Dependant dependant left join fetch dependant.congregant",
        countQuery = "select count(distinct dependant) from Dependant dependant"
    )
    Page<Dependant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct dependant from Dependant dependant left join fetch dependant.congregant")
    List<Dependant> findAllWithToOneRelationships();

    @Query("select dependant from Dependant dependant left join fetch dependant.congregant where dependant.id =:id")
    Optional<Dependant> findOneWithToOneRelationships(@Param("id") Long id);
}
