package org.thraex.toolkit.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import org.thraex.toolkit.entity.SoftEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author 鬼王
 * @date 2021/12/31 17:51
 */
@MappedSuperclass
public abstract class AbstractTree<T extends AbstractTree<T>>
        extends SoftEntity<T> implements Serializable {

    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    private String level;

    /**
     * TODO: Ignore parent ???
     */
    @OneToOne
    @JsonIncludeProperties({ "id", "name", "code" })
    private T parent;

    private boolean enabled;

    private String remark;

    /**
     * <pre>
     *     Remove {@code transient}
     *     Add:
     *     {@code @OneToMany(fetch = FetchType.EAGER)}
     *     {@code @JoinColumn(name = "parent_id")}
     * </pre>
     */
    private transient List<T> children = Collections.EMPTY_LIST;

    public String getName() {
        return name;
    }

    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getCode() {
        return code;
    }

    public T setCode(String code) {
        this.code = code;
        return (T) this;
    }

    public String getLevel() {
        return level;
    }

    public T setLevel(String level) {
        this.level = level;
        return (T) this;
    }

    public T getParent() {
        return parent;
    }

    public T setParent(T parent) {
        this.parent = parent;
        return (T) this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public String getRemark() {
        return remark;
    }

    public T setRemark(String remark) {
        this.remark = remark;
        return (T) this;
    }

    public List<T> getChildren() {
        return children;
    }

    public T setChildren(List<T> children) {
        this.children = children;
        return (T) this;
    }

    public static <E extends AbstractTree<E>> List<E> toTree(E root, List<E> list) {
        Objects.requireNonNull(list);

        boolean isNull = Objects.isNull(root);

        Predicate<E> predicate = isNull
                ? it -> Objects.isNull(it.getParent())
                : it -> Objects.nonNull(it.getParent()) && Objects.equals(it.getParent().getId(), root.getId());

        List<E> pool = isNull
                ? list.stream().filter(it -> Objects.nonNull(it.getParent())).collect(Collectors.toList())
                : list;

        return list.stream()
                .filter(predicate)
                .peek(it -> it.setChildren(toTree(it, pool)))
                .collect(Collectors.toList());
    }

}
