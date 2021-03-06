package org.thraex.toolkit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;

/**
 * @author 鬼王
 * @date 2021/12/28 16:23
 */
@MappedSuperclass
public abstract class SoftEntity<T extends SoftEntity<T>> extends JpaEntity<T> {

    @JsonIgnore
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public T setDeleted(boolean deleted) {
        this.deleted = deleted;
        return (T) this;
    }

}
