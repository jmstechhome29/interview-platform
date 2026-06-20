package com.interview.interviewservice.service;

import com.interview.interviewservice.client.CandidateClient;
import com.interview.interviewservice.client.InterviewerClient;
import com.interview.interviewservice.dto.InterviewDto;
import com.interview.interviewservice.dto.ScheduleInterviewRequest;
import com.interview.interviewservice.entity.Interview;
import com.interview.interviewservice.entity.InterviewStatus;
import com.interview.interviewservice.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final CandidateClient candidateClient;
    private final InterviewerClient interviewerClient;

    public InterviewService(InterviewRepository interviewRepository,
                             CandidateClient candidateClient,
                             InterviewerClient interviewerClient) {
        this.interviewRepository = interviewRepository;
        this.candidateClient = candidateClient;
        this.interviewerClient = interviewerClient;
    }

    public InterviewDto schedule(ScheduleInterviewRequest request) {
        // inter-service REST calls: validate + enrich with names
        String candidateName = candidateClient.fetchCandidateName(request.getCandidateId());
        String interviewerName = interviewerClient.fetchInterviewerName(request.getInterviewerId());

        Interview interview = new Interview();
        interview.setCandidateId(request.getCandidateId());
        interview.setCandidateName(candidateName);
        interview.setInterviewerId(request.getInterviewerId());
        interview.setInterviewerName(interviewerName);
        interview.setScheduledAt(request.getScheduledAt());
        interview.setNotes(request.getNotes());
        interview.setStatus(InterviewStatus.SCHEDULED);

        return InterviewDto.from(interviewRepository.save(interview));
    }

    public List<InterviewDto> listAll() {
        return interviewRepository.findAll().stream().map(InterviewDto::from).toList();
    }

    public List<InterviewDto> listForInterviewer(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId).stream().map(InterviewDto::from).toList();
    }

    public InterviewDto getById(Long id) {
        return InterviewDto.from(findOrThrow(id));
    }

    public Interview findOrThrow(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found: " + id));
    }

    public InterviewDto updateStatus(Long id, InterviewStatus status) {
        Interview interview = findOrThrow(id);
        interview.setStatus(status);
        return InterviewDto.from(interviewRepository.save(interview));
    }
}
