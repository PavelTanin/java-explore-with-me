package ru.practicum.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enums.State;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorOrderByIdAsc(User user, Pageable pageable);

    @Query("select e from Event as e " +
            "where (coalesce(:usersIds , null) is null or e.initiator.id in :usersIds) " +
            "and (coalesce(:states , null) is null or e.state in :states) " +
            "and (coalesce(:cats , null) is null or e.category.id in :cats) " +
            "and (coalesce(:start , null) is null or e.eventDate >= :start) " +
            "and (coalesce(:end , null) is null or e.eventDate <= :end)")
    Page<Event> getEventsByAdmin(List<Long> usersIds, List<State> states, List<Long> cats,
                     LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("select e from Event as e " +
            "where (coalesce(:text , null) is null or lower(e.annotation) like %:text% " +
            "or lower(e.description) like %:text%) " +
            "and (coalesce(:cats , null) is null or e.category.id in :cats) " +
            "and (coalesce(:paid , null) is null or e.paid = :paid) " +
            "and (coalesce(:start , null) is null or e.eventDate >= :start) " +
            "and (coalesce(:end , null) is null or e.eventDate <= :end)")
    List<Event> getEvents(String text, List<Long> cats, Boolean paid, LocalDateTime start,
                          LocalDateTime end, Pageable pageable);
}
