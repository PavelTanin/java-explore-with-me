package ru.practicum.model.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "compilations")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "pinned")
    private Boolean pinned;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(name = "comp_events",
    joinColumns = { @JoinColumn(name = "compilation_id")},
    inverseJoinColumns = { @JoinColumn(name = "event_id")})
    private List<Event> events;

    public Compilation(String title, Boolean pinned) {
        this.title = title;
        this.pinned = pinned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
