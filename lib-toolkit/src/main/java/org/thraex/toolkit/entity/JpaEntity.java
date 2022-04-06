package org.thraex.toolkit.entity;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author 鬼王
 * @date 2021/07/16 16:45
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaEntity<T extends JpaEntity<T>> {

    /**
     * @see {@link org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory}
     */
    @Id
    @Column(length = 36)
    @GeneratedValue(generator= "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    protected String id;

    @Column(length = 36)
    @CreatedBy
    protected String createdBy;

    @CreatedDate
    protected LocalDateTime createdDate;

    @Column(length = 36)
    @LastModifiedBy
    protected String modifiedBy;

    @LastModifiedDate
    protected LocalDateTime modifiedDate;

    public String getId() {
        return id;
    }

    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public T setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return (T) this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public T setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return (T) this;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public T setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return (T) this;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public T setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return (T) this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
