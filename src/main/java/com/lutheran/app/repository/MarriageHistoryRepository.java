package com.lutheran.app.repository;

import com.lutheran.app.domain.MarriageHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MarriageHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarriageHistoryRepository extends JpaRepository<MarriageHistory, Long> {}
