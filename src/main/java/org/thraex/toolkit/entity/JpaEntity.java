package org.thraex.toolkit.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 鬼王
 * @date 2021/07/16 16:45
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaEntity<E extends JpaEntity<E>> implements Serializable {

    /**
     * @see {@link org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory}
     */
    @Id
    @Column(length = 36)
    @GeneratedValue(generator= "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    private String id;

    @Column(length = 36)
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(length = 36)
    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public String getId() {
        return id;
    }

    public E setId(String id) {
        this.id = id;
        return (E) this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public E setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return (E) this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public E setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return (E) this;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public E setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return (E) this;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public E setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return (E) this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
