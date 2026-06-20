package com.interview.userservice.config;

import com.interview.userservice.entity.Role;
import com.interview.userservice.entity.User;
import com.interview.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", passwordEncoder.encode("admin123"),
                    "Platform Admin", "admin@interview.com", Role.ADMIN));
            userRepository.save(new User("interviewer1", passwordEncoder.encode("pass123"),
                    "Jane Interviewer", "jane@interview.com", Role.INTERVIEWER));
            userRepository.save(new User("interviewer2", passwordEncoder.encode("pass123"),
                    "John Interviewer", "john@interview.com", Role.INTERVIEWER));
        }
    }
}
