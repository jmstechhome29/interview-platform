package com.interview.interviewservice.dto;

import com.interview.interviewservice.entity.InterviewStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull
    private InterviewStatus status;

    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }
}
