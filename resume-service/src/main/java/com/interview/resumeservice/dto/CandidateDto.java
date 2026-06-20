package com.interview.resumeservice.dto;

import java.time.LocalDateTime;

public class CandidateDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String resumeFileName;
    private boolean hasResume;
    private LocalDateTime uploadedAt;

    public CandidateDto() {}

    public CandidateDto(Long id, String name, String email, String phone,
                         String resumeFileName, boolean hasResume, LocalDateTime uploadedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.resumeFileName = resumeFileName;
        this.hasResume = hasResume;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getResumeFileName() { return resumeFileName; }
    public void setResumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; }
    public boolean isHasResume() { return hasResume; }
    public void setHasResume(boolean hasResume) { this.hasResume = hasResume; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
