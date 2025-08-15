package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginAttempt {
    private AtomicInteger countOfAttempts = new AtomicInteger(0);
    private LocalDateTime blockedUntil;

    public void incrementCountOfAttempts() {
        int attempts = countOfAttempts.incrementAndGet();
        if (attempts >= 2) {
            blockedUntil = LocalDateTime.now().plusMinutes(5);
        }
    }

    public boolean isBlocked() {
        return blockedUntil != null && LocalDateTime.now().isBefore(blockedUntil);
    }

    public boolean isBlockedExpired() {
        return blockedUntil != null && LocalDateTime.now().isAfter(blockedUntil);
    }

    public void reset() {
        countOfAttempts.set(0);
        blockedUntil = null;
    }
}

