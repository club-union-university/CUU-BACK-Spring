package sms.uccbackend.global.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AiClient {

    private static final ParameterizedTypeReference<Map<String, Object>> JSON_OBJECT =
            new ParameterizedTypeReference<>() {};

    private final RestClient aiRestClient;

    public Map<String, Object> step1(Long eventId, Map<String, Object> payload) {
        return aiRestClient.post()
                .uri("/events/{id}/ai/step1", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(JSON_OBJECT);
    }

    public Map<String, Object> step2(Long eventId, Map<String, Object> payload) {
        return aiRestClient.post()
                .uri("/events/{id}/ai/step2", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(JSON_OBJECT);
    }
}
