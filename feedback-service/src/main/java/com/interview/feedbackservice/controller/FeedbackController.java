package com.interview.feedbackservice.controller;

import com.interview.feedbackservice.dto.FeedbackDto;
import com.interview.feedbackservice.dto.FeedbackRequest;
import com.interview.feedbackservice.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Interview Feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public FeedbackDto submit(@Valid @RequestBody FeedbackRequest request) {
        return feedbackService.submitFeedback(request);
    }

    @GetMapping
    public List<FeedbackDto> listAll() {
        return feedbackService.listAll();
    }

    @GetMapping("/interview/{interviewId}")
    public List<FeedbackDto> byInterview(@PathVariable Long interviewId) {
        return feedbackService.byInterview(interviewId);
    }

    @GetMapping("/interviewer/{interviewerId}")
    public List<FeedbackDto> byInterviewer(@PathVariable Long interviewerId) {
        return feedbackService.byInterviewer(interviewerId);
    }
}
