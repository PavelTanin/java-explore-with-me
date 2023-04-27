package ru.practicum.dto.compilation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;


import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class NewCompilationDto {

    @NotEmpty(message = "Название подборки не может быть пустым")
    private String title;

    @Value("false")
    private Boolean pinned;

    private List<Long> events;
}
