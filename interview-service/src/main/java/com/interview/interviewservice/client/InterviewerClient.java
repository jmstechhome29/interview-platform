package com.interview.interviewservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class InterviewerClient {

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url}")
    private String userServiceUrl;

    public InterviewerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public String fetchInterviewerName(Long interviewerId) {
        try {
            Map<String, Object> body = restTemplate.getForObject(
                    userServiceUrl + "/api/users/" + interviewerId, Map.class);
            return body == null ? null : String.valueOf(body.get("fullName"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Interviewer not found in user-service: " + interviewerId);
        }
    }
}
