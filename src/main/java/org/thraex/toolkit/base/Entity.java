package org.thraex.toolkit.base;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @author 鬼王
 * @date 2019/07/13 16:32
 */
public class Entity<E extends Entity<?>> implements Serializable {

    protected String id;

    protected String createBy;

    protected LocalDateTime createTime;

    protected String updateBy;

    protected LocalDateTime updateTime;

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
