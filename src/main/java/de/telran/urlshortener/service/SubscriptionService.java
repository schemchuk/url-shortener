package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionRequest;
import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.exceptionUser.UserNotFoundException;
import de.telran.urlshortener.repository.SubscriptionRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionResponse createSubscription(Long userId, SubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Subscription subscription = Subscription.builder()
                .user(user)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .subscriptionType(ConversionUtils.toSubscriptionEntityType(request.getSubscriptionType()))
                .build();

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return ConversionUtils.toSubscriptionResponse(savedSubscription);
    }
}







