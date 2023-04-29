package ru.practicum.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.enums.RequestStatus;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "participants")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "status")
    private RequestStatus status;

    public Request(User requestor, Event event, LocalDateTime created) {
        this.requester = requestor;
        this.event = event;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return id.equals(request.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
