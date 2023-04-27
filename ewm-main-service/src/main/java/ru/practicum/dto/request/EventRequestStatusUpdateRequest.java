package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.enums.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    @NotNull(message = "Список запросов не может быть пустым")
    private List<Long> requestIds;

    @NotNull(message = "Статус не может быть пустым")
    private RequestStatus status;
}
