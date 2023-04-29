package ru.practicum.dto;

import lombok.Data;

@Data
public class ViewStatsDto {

    private String app;

    private String uri;

    private Long hits;

    public ViewStatsDto() {
    }

    public ViewStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
