package org.sam.store.common.lock;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@EqualsAndHashCode
public class Lock {

    private final String id;
    private LocalDateTime expiredTime;

    public Lock(String id, long additionalMilliSeconds) {
        this.id = id;
        this.expiredTime = LocalDateTime.now().plusNanos(additionalMilliSeconds * 1000);
    }

    public void extendExpiredTime(long additionalNanoTime) {
        this.expiredTime = this.expiredTime.plusNanos(additionalNanoTime);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredTime);
    }

}
