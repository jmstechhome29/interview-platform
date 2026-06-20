package com.interview.interviewservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CandidateClient {

    private final RestTemplate restTemplate;

    @Value("${services.resume-service.url}")
    private String resumeServiceUrl;

    public CandidateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public String fetchCandidateName(Long candidateId) {
        try {
            Map<String, Object> body = restTemplate.getForObject(
                    resumeServiceUrl + "/api/candidates/" + candidateId, Map.class);
            return body == null ? null : String.valueOf(body.get("name"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Candidate not found in resume-service: " + candidateId);
        }
    }
}
