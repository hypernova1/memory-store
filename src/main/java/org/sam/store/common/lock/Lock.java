package org.sam.store.common.lock;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class Lock {

    private final String id;
    private LocalDateTime expiredTime;

    public Lock(String id, long additionalNanoTime) {
        this.id = id;
        this.expiredTime = LocalDateTime.now().plusNanos(additionalNanoTime);
    }

    public void extendExpiredTime(long additionalNanoTime) {
        this.expiredTime = this.expiredTime.plusNanos(additionalNanoTime);
    }

}
