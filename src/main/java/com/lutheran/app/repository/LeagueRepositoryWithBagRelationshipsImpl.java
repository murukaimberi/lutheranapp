package com.lutheran.app.repository;

import com.lutheran.app.domain.League;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class LeagueRepositoryWithBagRelationshipsImpl implements LeagueRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<League> fetchBagRelationships(Optional<League> league) {
        return league.map(this::fetchCongregants);
    }

    @Override
    public Page<League> fetchBagRelationships(Page<League> leagues) {
        return new PageImpl<>(fetchBagRelationships(leagues.getContent()), leagues.getPageable(), leagues.getTotalElements());
    }

    @Override
    public List<League> fetchBagRelationships(List<League> leagues) {
        return Optional.of(leagues).map(this::fetchCongregants).orElse(Collections.emptyList());
    }

    League fetchCongregants(League result) {
        return entityManager
            .createQuery("select league from League league left join fetch league.congregants where league is :league", League.class)
            .setParameter("league", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<League> fetchCongregants(List<League> leagues) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, leagues.size()).forEach(index -> order.put(leagues.get(index).getId(), index));
        List<League> result = entityManager
            .createQuery(
                "select distinct league from League league left join fetch league.congregants where league in :leagues",
                League.class
            )
            .setParameter("leagues", leagues)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
