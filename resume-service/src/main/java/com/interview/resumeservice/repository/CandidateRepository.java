package com.interview.resumeservice.repository;

import com.interview.resumeservice.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
