package org.sam.store.common;


import org.sam.store.common.repository.annotation.CreatedDate;
import org.sam.store.common.repository.annotation.UpdatedDate;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @UpdatedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
