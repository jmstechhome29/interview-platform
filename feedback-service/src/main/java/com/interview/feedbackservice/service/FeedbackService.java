package com.interview.feedbackservice.service;

import com.interview.feedbackservice.client.InterviewClient;
import com.interview.feedbackservice.dto.FeedbackDto;
import com.interview.feedbackservice.dto.FeedbackRequest;
import com.interview.feedbackservice.entity.Feedback;
import com.interview.feedbackservice.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final InterviewClient interviewClient;

    public FeedbackService(FeedbackRepository feedbackRepository, InterviewClient interviewClient) {
        this.feedbackRepository = feedbackRepository;
        this.interviewClient = interviewClient;
    }

    public FeedbackDto submitFeedback(FeedbackRequest request) {
        // inter-service REST call: confirm the interview exists, pull candidateId
        Map<String, Object> interview = interviewClient.fetchInterview(request.getInterviewId());
        Long candidateId = Long.valueOf(String.valueOf(interview.get("candidateId")));

        Feedback feedback = new Feedback();
        feedback.setInterviewId(request.getInterviewId());
        feedback.setCandidateId(candidateId);
        feedback.setInterviewerId(request.getInterviewerId());
        feedback.setRating(request.getRating());
        feedback.setRecommendation(request.getRecommendation());
        feedback.setComments(request.getComments());

        Feedback saved = feedbackRepository.save(feedback);

        // mark the interview as completed once feedback is in
        interviewClient.markCompleted(request.getInterviewId());

        return FeedbackDto.from(saved);
    }

    public List<FeedbackDto> listAll() {
        return feedbackRepository.findAll().stream().map(FeedbackDto::from).toList();
    }

    public List<FeedbackDto> byInterview(Long interviewId) {
        return feedbackRepository.findByInterviewId(interviewId).stream().map(FeedbackDto::from).toList();
    }

    public List<FeedbackDto> byInterviewer(Long interviewerId) {
        return feedbackRepository.findByInterviewerId(interviewerId).stream().map(FeedbackDto::from).toList();
    }
}
