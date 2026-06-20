package com.interview.resumeservice.controller;

import com.interview.resumeservice.dto.CandidateDto;
import com.interview.resumeservice.entity.Candidate;
import com.interview.resumeservice.service.CandidateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@Tag(name = "Candidates & Resumes")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CandidateDto createCandidate(@RequestParam String name,
                                         @RequestParam String email,
                                         @RequestParam(required = false) String phone,
                                         @RequestParam(required = false) MultipartFile resume) {
        return candidateService.createCandidate(name, email, phone, resume);
    }

    @PostMapping(value = "/{id}/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CandidateDto uploadResume(@PathVariable Long id, @RequestParam MultipartFile resume) {
        return candidateService.uploadResume(id, resume);
    }

    @GetMapping
    public List<CandidateDto> listCandidates() {
        return candidateService.listCandidates();
    }

    @GetMapping("/{id}")
    public CandidateDto getCandidate(@PathVariable Long id) {
        return candidateService.getCandidate(id);
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {
        Candidate candidate = candidateService.findOrThrow(id);
        Resource resource = candidateService.loadResumeFile(id);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(candidate.getResumeFileName() == null ? "resume" : candidate.getResumeFileName())
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.parseMediaType(
                        candidate.getResumeContentType() == null ? "application/octet-stream" : candidate.getResumeContentType()))
                .body(resource);
    }
}
