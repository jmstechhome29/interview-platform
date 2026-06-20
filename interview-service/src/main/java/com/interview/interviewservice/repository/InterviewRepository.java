package com.interview.interviewservice.repository;

import com.interview.interviewservice.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByInterviewerId(Long interviewerId);
    List<Interview> findByCandidateId(Long candidateId);
}
