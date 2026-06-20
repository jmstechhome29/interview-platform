package com.interview.interviewservice.dto;

import com.interview.interviewservice.entity.Interview;
import com.interview.interviewservice.entity.InterviewStatus;

import java.time.LocalDateTime;

public class InterviewDto {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long interviewerId;
    private String interviewerName;
    private LocalDateTime scheduledAt;
    private InterviewStatus status;
    private String notes;

    public static InterviewDto from(Interview i) {
        InterviewDto dto = new InterviewDto();
        dto.id = i.getId();
        dto.candidateId = i.getCandidateId();
        dto.candidateName = i.getCandidateName();
        dto.interviewerId = i.getInterviewerId();
        dto.interviewerName = i.getInterviewerName();
        dto.scheduledAt = i.getScheduledAt();
        dto.status = i.getStatus();
        dto.notes = i.getNotes();
        return dto;
    }

    public Long getId() { return id; }
    public Long getCandidateId() { return candidateId; }
    public String getCandidateName() { return candidateName; }
    public Long getInterviewerId() { return interviewerId; }
    public String getInterviewerName() { return interviewerName; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public InterviewStatus getStatus() { return status; }
    public String getNotes() { return notes; }
}
