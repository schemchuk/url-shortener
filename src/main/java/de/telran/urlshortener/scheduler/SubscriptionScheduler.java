package de.telran.urlshortener.scheduler;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Запуск каждый день в полночь
    public void deleteExpiredSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        try {
            List<Subscription> expiredSubscriptions = subscriptionRepository.findByEndDateBefore(now);
            if (!expiredSubscriptions.isEmpty()) {
                subscriptionRepository.deleteAll(expiredSubscriptions);
                log.info("Deleted {} expired subscriptions at {}", expiredSubscriptions.size(), now);
            } else {
                log.info("No expired subscriptions to delete at {}", now);
            }
        } catch (Exception e) {
            log.error("Failed to delete expired subscriptions at {} due to {}", now, e.getMessage());
        }
    }
}

