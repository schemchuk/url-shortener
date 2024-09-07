package de.telran.urlshortener.security;

import de.telran.urlshortener.dto.JwtDto.JwtRequest;
import de.telran.urlshortener.dto.JwtDto.JwtResponse;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.AuthException;
import de.telran.urlshortener.exception.InvalidJwtTokenException;
import de.telran.urlshortener.exception.UserNotFoundException;
import de.telran.urlshortener.service.userService.UserLookupService;
import de.telran.urlshortener.service.userService.UserPersistenceService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * The service for looking up users (e.g., by login or email).
     */
    private final UserLookupService userLookupService;

    /**
     * The service for persisting user data (saving/updating).
     */
    private final UserPersistenceService userPersistenceService;

    /**
     * The JWT provider for generating and validating JWT tokens.
     */
    private final JwtProvider jwtProvider;

    /**
     * Password encoder to match passwords.
     */
    private final PasswordEncoder encoder;

    /**
     * Handles user login and returns JWT tokens upon successful authentication.
     * Saves the refresh token in the User entity and updates the database.
     *
     * @param authRequest the authentication request containing user credentials.
     * @return a JwtResponse containing the generated access and refresh tokens.
     * @throws AuthException if the user is not found or the password is incorrect.
     */
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        // Fetch the user by login
        final User user = userLookupService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if the provided password matches the stored password
        if (encoder.matches(authRequest.getPassword(), user.getPassword())) {
            // Generate access and refresh tokens
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);

            // Save the refresh token in the user entity and update the database
            user.setRefreshToken(refreshToken);
            userPersistenceService.save(user);  // Save the updated user object with the refresh token

            // Return the access and refresh tokens
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Wrong password");
        }
    }

    /**
     * Generates a new access token using a valid refresh token.
     *
     * This method first validates the provided refresh token using the {@link JwtProvider}.
     * If the token is valid, it extracts the user login from the token claims,
     * retrieves the user, compares the stored refresh token with the provided token,
     * and generates a new access token if they match.
     *
     * @param refreshToken the refresh token.
     * @return a JwtResponse containing the generated access token, or null values if validation fails.
     * @throws AuthException if the user is not found or the refresh token is invalid.
     */
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        // Validate the provided refresh token
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            // Extract claims from the refresh token
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            // Get the user login from the token claims
            final String login = claims.getSubject();
            // Fetch the user from the database
            final User user = userLookupService.getByLogin(login)
                    .orElseThrow(() -> new UserNotFoundException("User is not found"));

            // Compare the stored refresh token with the provided token
            if (user.getRefreshToken() != null && user.getRefreshToken().equals(refreshToken)) {
                // Generate a new access token
                final String accessToken = jwtProvider.generateAccessToken(user);
                // Return a JwtResponse with the new access token
                return new JwtResponse(accessToken, null);
            }
        }
        // Throw an exception if validation fails
        throw new InvalidJwtTokenException("Invalid refresh token");
    }

    /**
     * Refreshes both access and refresh tokens using a valid refresh token.
     *
     * This method validates the provided refresh token, generates new access
     * and refresh tokens, updates the stored refresh token in the user entity,
     * and updates the user in the database.
     *
     * @param refreshToken the refresh token.
     * @return a JwtResponse containing the generated access and refresh tokens.
     * @throws AuthException if the refresh token is invalid or the user is not found.
     */
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        // Validate the provided refresh token
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            // Extract claims from the refresh token
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            // Get the user login from the token claims
            final String login = claims.getSubject();
            // Fetch the user from the database
            final User user = userLookupService.getByLogin(login)
                    .orElseThrow(() -> new UserNotFoundException("User is not found"));

            // Compare the stored refresh token with the provided token
            if (user.getRefreshToken() != null && user.getRefreshToken().equals(refreshToken)) {
                // Generate new access and refresh tokens
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);

                // Update the refresh token in the user entity and save it to the database
                user.setRefreshToken(newRefreshToken);
                userPersistenceService.save(user);

                // Return a JwtResponse with the new access and refresh tokens
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        // Throw an exception if validation fails
        throw new InvalidJwtTokenException("Invalid refresh token");
    }

    /**
     * Retrieves the authentication information from the security context.
     *
     * @return the JwtAuthentication object containing the authentication information.
     */
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
