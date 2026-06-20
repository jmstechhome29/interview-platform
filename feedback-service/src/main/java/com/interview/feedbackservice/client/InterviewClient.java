package com.interview.feedbackservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class InterviewClient {

    private final RestTemplate restTemplate;

    @Value("${services.interview-service.url}")
    private String interviewServiceUrl;

    public InterviewClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchInterview(Long interviewId) {
        try {
            return restTemplate.getForObject(
                    interviewServiceUrl + "/api/interviews/" + interviewId, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Interview not found in interview-service: " + interviewId);
        }
    }

    public void markCompleted(Long interviewId) {
        restTemplate.put(interviewServiceUrl + "/api/interviews/" + interviewId + "/status",
                Map.of("status", "COMPLETED"));
    }
}
