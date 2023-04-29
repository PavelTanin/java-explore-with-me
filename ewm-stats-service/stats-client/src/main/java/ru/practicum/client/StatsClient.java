package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class StatsClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String uri = "http://stats-service:9090";

    public String hit(HttpServletRequest request, String appName) throws JsonProcessingException {
        EndpointHitDto hit = new EndpointHitDto(appName, request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());
        return restTemplate.postForEntity(uri + "/hit", hit, String.class).getBody();
    }

    public List<ViewStatsDto> getStats(List<String> uris, String start, String end) {
        String url = uri + "/stats?start=" + start + "&end=" + end + "&uris=" + uris.toString()
                .replace("[", "").replace("]", "");
        return List.of(restTemplate.getForEntity(url, ViewStatsDto[].class).getBody());
    }


}
