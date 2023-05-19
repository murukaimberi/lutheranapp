package com.lutheran.app.repository;

import com.lutheran.app.domain.League;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LeagueRepositoryWithBagRelationships {
    Optional<League> fetchBagRelationships(Optional<League> league);

    List<League> fetchBagRelationships(List<League> leagues);

    Page<League> fetchBagRelationships(Page<League> leagues);
}
