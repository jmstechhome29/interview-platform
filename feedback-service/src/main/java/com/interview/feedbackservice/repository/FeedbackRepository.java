package com.interview.feedbackservice.repository;

import com.interview.feedbackservice.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByInterviewId(Long interviewId);
    List<Feedback> findByInterviewerId(Long interviewerId);
}
