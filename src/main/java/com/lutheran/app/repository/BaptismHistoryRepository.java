package com.lutheran.app.repository;

import com.lutheran.app.domain.BaptismHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaptismHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaptismHistoryRepository extends JpaRepository<BaptismHistory, Long> {}
