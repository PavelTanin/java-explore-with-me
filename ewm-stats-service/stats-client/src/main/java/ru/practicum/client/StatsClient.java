package ru.practicum.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import ru.practicum.dto.EndpointHitDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class StatsClient {

    private final String application;

    private final String statsServiceUri;

    private final ObjectMapper json;

    private final HttpClient httpClient;

    @Autowired
    public StatsClient(@Value("${spring.application.name") String application,
                       @Value("${services.stats-service.uri:http://localhost:9090}") String statsServiceUri,
                       ObjectMapper json) {
        this.application = application;
        this.statsServiceUri = statsServiceUri;
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public void hit() throws Exception {
        EndpointHitDto hit = new EndpointHitDto();
        hit.setApp(application);

        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
                    .BodyPublishers
                    .ofString(json.writeValueAsString(hit));

            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            HttpResponse<Void> resource = httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }


}
