package com.interview.interviewservice.controller;

import com.interview.interviewservice.dto.InterviewDto;
import com.interview.interviewservice.dto.ScheduleInterviewRequest;
import com.interview.interviewservice.dto.StatusUpdateRequest;
import com.interview.interviewservice.service.InterviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@Tag(name = "Interview Scheduling")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public InterviewDto schedule(@Valid @RequestBody ScheduleInterviewRequest request) {
        return interviewService.schedule(request);
    }

    @GetMapping
    public List<InterviewDto> listAll() {
        return interviewService.listAll();
    }

    @GetMapping("/interviewer/{interviewerId}")
    public List<InterviewDto> listForInterviewer(@PathVariable Long interviewerId) {
        return interviewService.listForInterviewer(interviewerId);
    }

    @GetMapping("/{id}")
    public InterviewDto getById(@PathVariable Long id) {
        return interviewService.getById(id);
    }

    @PutMapping("/{id}/status")
    public InterviewDto updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return interviewService.updateStatus(id, request.getStatus());
    }
}
