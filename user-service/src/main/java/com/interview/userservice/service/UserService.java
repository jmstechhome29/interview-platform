package com.interview.userservice.service;

import com.interview.userservice.dto.UserDto;
import com.interview.userservice.entity.Role;
import com.interview.userservice.entity.User;
import com.interview.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getInterviewers() {
        return userRepository.findByRole(Role.INTERVIEWER).stream().map(this::toDto).toList();
    }

    public UserDto getById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        return toDto(u);
    }

    private UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getRole().name());
    }
}
