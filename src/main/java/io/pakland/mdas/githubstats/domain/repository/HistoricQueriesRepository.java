package io.pakland.mdas.githubstats.domain.repository;

import io.pakland.mdas.githubstats.domain.entity.HistoricQueries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricQueriesRepository extends JpaRepository<HistoricQueries, Integer> {
}