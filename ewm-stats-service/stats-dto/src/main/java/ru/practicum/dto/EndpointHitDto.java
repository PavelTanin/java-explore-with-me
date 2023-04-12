package ru.practicum.dto;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EndpointHitDto {

    private String app;

    private String uri;

    private String ip;

    private String timestamp;



}
