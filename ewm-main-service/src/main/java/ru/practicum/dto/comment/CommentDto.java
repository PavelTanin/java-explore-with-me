package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    private Long event_id;

    private Long user_id;

    @NotEmpty(message = "Нельзя добавить комментарий без текста")
    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updateTime;

    public CommentDto(Long id, Long event_id, Long user_id, String text, LocalDateTime createdTime) {
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.text = text;
        this.createdTime = createdTime;
    }
}
