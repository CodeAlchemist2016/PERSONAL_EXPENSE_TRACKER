package com.medvedev.backend.service;

import com.medvedev.backend.entity.User;
import com.medvedev.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    public Optional<User> findUserByEmail(String email) {

        return userRepository.findByEmailIgnoreCase(email);
    }

    public void deleteUserById(Integer id ) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
    }

    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }
}
