package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.UserNameAlreadyTakenException;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(String userName, String email, String password) {
        validateEmail(email);
        validatePassword(password);

        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        // Auto-assign TRIAL role and subscription here if needed

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null) { // Handle password update
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserNameAlreadyTakenException("Email already in use");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }
    }
}
