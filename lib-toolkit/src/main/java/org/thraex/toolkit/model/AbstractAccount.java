package org.thraex.toolkit.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.thraex.toolkit.entity.SoftEntity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

/**
 * TODO: Authorities
 *
 * @author 鬼王
 * @date 2021/12/31 09:59
 */
@MappedSuperclass
public abstract class AbstractAccount<T extends AbstractAccount<T>>
        extends SoftEntity<T> implements Serializable {

    private String nickname;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String email;

    private String mobile;

    private boolean enabled;

    private boolean locked;

    private LocalDate expires;

    public AbstractAccount() { }

    public AbstractAccount(String nickname, String username, String password, String email,
                           String mobile, boolean enabled, boolean locked, LocalDate expires) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.enabled = enabled;
        this.locked = locked;
        this.expires = expires;
    }

    public String getNickname() {
        return nickname;
    }

    public T setNickname(String nickname) {
        this.nickname = nickname;
        return (T) this;
    }

    public String getUsername() {
        return username;
    }

    public T setUsername(String username) {
        this.username = username;
        return (T) this;
    }

    public String getPassword() {
        return password;
    }

    public T setPassword(String password) {
        this.password = password;
        return (T) this;
    }

    public String getEmail() {
        return email;
    }

    public T setEmail(String email) {
        this.email = email;
        return (T) this;
    }

    public String getMobile() {
        return mobile;
    }

    public T setMobile(String mobile) {
        this.mobile = mobile;
        return (T) this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public boolean isLocked() {
        return locked;
    }

    public T setLocked(boolean locked) {
        this.locked = locked;
        return (T) this;
    }

    public LocalDate getExpires() {
        return expires;
    }

    public T setExpires(LocalDate expires) {
        this.expires = expires;
        return (T) this;
    }

    public boolean isExpired() {
        return Optional.ofNullable(expires).map(e -> e.isAfter(LocalDate.now())).orElse(false);
    }

}
