package org.thraex.toolkit.security.properties;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.thraex.toolkit.security.constant.AuthenticationMethod;

import java.util.Collections;
import java.util.Set;

/**
 * @author 鬼王
 * @date 2022/05/15 00:29
 */
@ConfigurationProperties("thraex.security")
public class SecurityProperties {

    private Set<String> permitted = Collections.EMPTY_SET;

    private AuthenticationMethod authenticationMethod = AuthenticationMethod.USERNAME_PASSWORD;

    public Set<String> getPermitted() {
        return permitted;
    }

    public SecurityProperties setPermitted(Set<String> permitted) {
        this.permitted = permitted;
        return this;
    }

    public AuthenticationMethod getAuthenticationMethod() {
        return authenticationMethod;
    }

    public SecurityProperties setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
