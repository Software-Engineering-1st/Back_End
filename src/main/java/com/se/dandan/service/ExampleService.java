package com.se.dandan.service;

import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExampleService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8000")
            .build();

    public String generateExample(String word, List<String> meaning) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("word", word);
        requestBody.put("meaning", meaning);

        return webClient.post()
                .uri("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ExampleResponse.class)
                .block()
                .getAnswer();
    }

    @Data
    static class ExampleResponse {
        private String answer;
    }
}
