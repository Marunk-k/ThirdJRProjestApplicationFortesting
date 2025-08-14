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
    private LocalDateTime lastLogin;

    public void incrementCountOfAttempts() {
        countOfAttempts.getAndIncrement();
        lastLogin = LocalDateTime.now();
    }

    public boolean isBlocked() {
        return (lastLogin == null || (countOfAttempts.get() >= 3 && !lastLogin.isBefore(LocalDateTime.now().minusMinutes(5))));
    }

    public boolean isBlockedExpired() {
        return lastLogin == null || lastLogin.isBefore(LocalDateTime.now().minusMinutes(5));
    }
}
