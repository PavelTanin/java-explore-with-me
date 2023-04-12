package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(eh.uri)) from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 group by eh.app, eh.uri")
    List<ViewStats> findAllHitsTimestampBetweenAndNotUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(eh.uri)) from EndpointHit as eh where eh.uri in ?3 " +
            "and eh.timestamp between ?1 and ?2 group by eh.app, eh.uri")
    List<ViewStats> findAllHitsTimestampBetweenAndUriInAndNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(distinct(eh.ip))) from EndpointHit as eh " +
            "where eh.timestamp between ?1 and ?2 group by eh.app, eh.uri")
    List<ViewStats> findAllHitsTimestampBetweenAndUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.model.ViewStats(eh.app, eh.uri, count(distinct(eh.ip))) from EndpointHit as eh where eh.uri in ?3 " +
            "and eh.timestamp between ?1 and ?2 group by eh.app, eh.uri")
    List<ViewStats> findAllHitsTimestampBetweenAndUriInAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);


}
