package com.interview.feedbackservice.dto;

import com.interview.feedbackservice.entity.Recommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FeedbackRequest {
    @NotNull
    private Long interviewId;
    @NotNull
    private Long interviewerId;
    @NotNull
    @Min(1) @Max(5)
    private Integer rating;
    @NotNull
    private Recommendation recommendation;
    private String comments;

    public Long getInterviewId() { return interviewId; }
    public void setInterviewId(Long interviewId) { this.interviewId = interviewId; }
    public Long getInterviewerId() { return interviewerId; }
    public void setInterviewerId(Long interviewerId) { this.interviewerId = interviewerId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public Recommendation getRecommendation() { return recommendation; }
    public void setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
