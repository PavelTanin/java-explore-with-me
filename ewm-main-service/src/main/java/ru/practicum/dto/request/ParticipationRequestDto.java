package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.enums.RequestStatus;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {

    private String created;

    private Long event;

    private Long id;

    private Long requester;

    private RequestStatus status;

}
