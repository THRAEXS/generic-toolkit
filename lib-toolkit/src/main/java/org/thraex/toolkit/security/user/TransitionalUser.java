package org.thraex.toolkit.security.user;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO: Refactor
 *
 * @author 鬼王
 * @date 2022/04/25 22:02
 */
public class TransitionalUser implements UserDetails {

    private String id;

    private String username;

    private String password;

    private Set<GrantedAuthority> authorities;

    public TransitionalUser(String id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(id != null && !"".equals(id) && username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");

        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    public String getId() {
        return id;
    }

    public TransitionalUser setId(String id) {
        this.id = id;
        return this;
    }

    public TransitionalUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public TransitionalUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public TransitionalUser setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static UserBuilder withId(String id) {
        return new UserBuilder().id(id);
    }

    public static UserBuilder withMap(Map<String, Object> principal) {
        return withId(principal.get("id").toString())
                .username(principal.get("username").toString())
                .password(principal.get("password").toString())
                .roles((List<String>) principal.get("authorities"));
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");

        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }

    }

    public static final class UserBuilder {

        private String id;

        private String username;

        private String password;

        private List<GrantedAuthority> authorities;

        public UserBuilder id(String id) {
            Assert.notNull(id, "id cannot be null");
            this.id = id;

            return this;
        }

        public UserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;

            return this;
        }

        public UserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;

            return this;
        }

        public UserBuilder roles(List<String> roles) {
            Assert.notNull(roles, "roles cannot be null");

            List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
            roles.forEach(it -> authorities.add(new SimpleGrantedAuthority(it)));

            return authorities(authorities);
        }

        public UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public UserDetails build() {
            return new TransitionalUser(id, username, password, authorities);
        }

    }

}
