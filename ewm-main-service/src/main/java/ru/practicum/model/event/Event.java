package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.enums.State;
import ru.practicum.model.category.Category;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.location.Location;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "pinned")
    private Boolean pinned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "comp_events",
            joinColumns = { @JoinColumn(name = "event_id")},
            inverseJoinColumns = { @JoinColumn(name = "compilation_id")})
    private List<Compilation> compilations;

    @OneToMany(mappedBy = "event")
    private List<Request> requests;

    @Transient
    private Long views;

    public Event(String title, String annotation, String description,
                 LocalDateTime eventDate, Boolean paid, Long participantLimit,
                 Boolean requestModeration, Location location) {
        this.title = title;
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
