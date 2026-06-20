package com.interview.resumeservice.service;

import com.interview.resumeservice.config.StorageProperties;
import com.interview.resumeservice.dto.CandidateDto;
import com.interview.resumeservice.entity.Candidate;
import com.interview.resumeservice.repository.CandidateRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final StorageProperties storageProperties;

    public CandidateService(CandidateRepository candidateRepository, StorageProperties storageProperties) {
        this.candidateRepository = candidateRepository;
        this.storageProperties = storageProperties;
        try {
            Files.createDirectories(Paths.get(storageProperties.getLocation()));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize resume storage directory", e);
        }
    }

    public CandidateDto createCandidate(String name, String email, String phone, MultipartFile resume) {
        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidate.setEmail(email);
        candidate.setPhone(phone);

        if (resume != null && !resume.isEmpty()) {
            storeResume(candidate, resume);
        }
        Candidate saved = candidateRepository.save(candidate);
        return toDto(saved);
    }

    public CandidateDto uploadResume(Long candidateId, MultipartFile resume) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + candidateId));
        storeResume(candidate, resume);
        return toDto(candidateRepository.save(candidate));
    }

    private void storeResume(Candidate candidate, MultipartFile resume) {
        try {
            String safeName = UUID.randomUUID() + "_" + resume.getOriginalFilename();
            Path target = Paths.get(storageProperties.getLocation(), safeName);
            resume.transferTo(target);
            candidate.setResumeFileName(resume.getOriginalFilename());
            candidate.setResumeContentType(resume.getContentType());
            candidate.setResumeStoragePath(target.toString());
            candidate.setUploadedAt(LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store resume file", e);
        }
    }

    public List<CandidateDto> listCandidates() {
        return candidateRepository.findAll().stream().map(this::toDto).toList();
    }

    public CandidateDto getCandidate(Long id) {
        return toDto(findOrThrow(id));
    }

    public Candidate findOrThrow(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + id));
    }

    public Resource loadResumeFile(Long id) {
        Candidate candidate = findOrThrow(id);
        if (candidate.getResumeStoragePath() == null) {
            throw new IllegalArgumentException("No resume uploaded for candidate: " + id);
        }
        return new FileSystemResource(candidate.getResumeStoragePath());
    }

    private CandidateDto toDto(Candidate c) {
        return new CandidateDto(c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                c.getResumeFileName(), c.getResumeStoragePath() != null, c.getUploadedAt());
    }
}
