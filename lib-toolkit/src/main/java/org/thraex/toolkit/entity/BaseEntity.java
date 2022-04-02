package org.thraex.toolkit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @author 鬼王
 * @date 2019/07/13 16:32
 */
public class BaseEntity<E extends BaseEntity<E>> implements Serializable {

    private String id;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public BaseEntity() { }

    public BaseEntity(String createBy, LocalDateTime createTime) {
        this.createBy = createBy;
        this.createTime = createTime;
    }

    public BaseEntity(String id, String updateBy, LocalDateTime updateTime) {
        this.id = id;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
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

    public E snapshot(String by) {
        LocalDateTime now = LocalDateTime.now();

        return (E) (this.id == null || this.id.trim().length() == 0
                ? this.setCreateBy(by).setCreateTime(now)
                : this.setUpdateBy(by).setUpdateTime(now));
    }

    public E snapshot(Supplier<String> by) {
        return this.snapshot(by.get());
    }

}
