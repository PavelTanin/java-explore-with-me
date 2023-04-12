package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "endpointhits")
@Setter
@Getter
@AllArgsConstructor
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public EndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public EndpointHit() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHit that = (EndpointHit) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
