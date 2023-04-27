package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterOrderByIdAsc(User user);

    List<Request> findAllByEventOrderByIdAsc(Event event);

    List<Request> findAllByIdInOrderById(List<Long> requestIds);

    Request findByRequesterIdAndEventId(Long userId, Long eventId);
}
