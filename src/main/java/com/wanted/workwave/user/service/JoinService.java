package com.wanted.workwave.user.service;

import com.wanted.workwave.user.domain.User;
import com.wanted.workwave.user.domain.UserRepository;
import com.wanted.workwave.user.dto.JoinRequest;
import com.wanted.workwave.user.dto.JoinResponse;
import com.wanted.workwave.user.exception.DuplicateUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JoinResponse join(JoinRequest request) {
        validateDuplicateUsername(request.getUsername());

        User user = userRepository.save(request.toEntity());
        user.changePassword(passwordEncoder.encode(request.getPassword()));

        return new JoinResponse(user.getId(), user.getUsername());
    }

    private void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException();
        }
    }

}
