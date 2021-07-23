package org.thraex.toolkit.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
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
public class JpaEntity<E extends JpaEntity<E>> implements Serializable {

    protected static final int IDENTIFIER_LENGTH = 36;

    /**
     * @see {@link org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory}
     */
    @Id
    @Column(length = IDENTIFIER_LENGTH)
    @GeneratedValue(generator= "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    private String id;

    @Column(length = IDENTIFIER_LENGTH)
    private String createBy;

    private LocalDateTime createTime;

    @Column(length = IDENTIFIER_LENGTH)
    private String updateBy;

    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public E setId(String id) {
        this.id = id;
        return (E) this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public E setCreateBy(String createBy) {
        this.createBy = createBy;
        return (E) this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public E setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return (E) this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public E setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return (E) this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public E setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return (E) this;
    }

}
