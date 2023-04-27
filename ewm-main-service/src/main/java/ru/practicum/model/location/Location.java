package ru.practicum.model.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "locations")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull(message = "Некоректно указаны координаты места проведения")
    @Column(name = "lat")
    private Float lat;

    @NotNull(message = "Некоректно указаны координаты места проведения")
    @Column(name = "lon")
    private Float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
