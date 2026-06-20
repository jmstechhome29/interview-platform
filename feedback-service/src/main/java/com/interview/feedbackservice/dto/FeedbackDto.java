package com.interview.feedbackservice.dto;

import com.interview.feedbackservice.entity.Feedback;
import com.interview.feedbackservice.entity.Recommendation;

import java.time.LocalDateTime;

public class FeedbackDto {
    private Long id;
    private Long interviewId;
    private Long candidateId;
    private Long interviewerId;
    private Integer rating;
    private Recommendation recommendation;
    private String comments;
    private LocalDateTime createdAt;

    public static FeedbackDto from(Feedback f) {
        FeedbackDto dto = new FeedbackDto();
        dto.id = f.getId();
        dto.interviewId = f.getInterviewId();
        dto.candidateId = f.getCandidateId();
        dto.interviewerId = f.getInterviewerId();
        dto.rating = f.getRating();
        dto.recommendation = f.getRecommendation();
        dto.comments = f.getComments();
        dto.createdAt = f.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getInterviewId() { return interviewId; }
    public Long getCandidateId() { return candidateId; }
    public Long getInterviewerId() { return interviewerId; }
    public Integer getRating() { return rating; }
    public Recommendation getRecommendation() { return recommendation; }
    public String getComments() { return comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
