package de.telran.urlshortener.scheduler;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.SubscriptionRepository;
import de.telran.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private static final Logger log = LogManager.getLogger(SubscriptionScheduler.class);

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Запуск каждый день в полночь
    public void deleteExpiredSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByEndDateBefore(now);

        if (expiredSubscriptions.isEmpty()) {
            log.info("No expired subscriptions to delete at {}", now);
            return;
        }

        expiredSubscriptions.forEach(subscription -> {
            try {
                deleteUserAndSubscription(subscription);
                log.info("Deleted user {} and associated subscription at {}", subscription.getUser().getEmail(), now);
            } catch (Exception e) {
                log.error("Failed to delete user {} at {} due to {}", subscription.getUser().getEmail(), now, e.getMessage());
            }
        });
    }

    private void deleteUserAndSubscription(Subscription subscription) {
        User user = subscription.getUser();
        clearUserRoles(user);

        // Удаляем подписку
        subscriptionRepository.delete(subscription);

        // Удаляем пользователя, если у него нет ролей и он не связан с другими подписками
        if (user.getRoles().isEmpty() && !userHasActiveSubscription(user)) {
            userRepository.delete(user);
        }
    }

    private void clearUserRoles(User user) {
        user.getRoles().clear(); // Удаляем роли пользователя
        userRepository.save(user); // Сохраняем изменения пользователя без ролей
    }

    private boolean userHasActiveSubscription(User user) {
        // Проверяем, имеет ли пользователь активные подписки
        return subscriptionRepository.findByUser(user) != null;
    }
}
