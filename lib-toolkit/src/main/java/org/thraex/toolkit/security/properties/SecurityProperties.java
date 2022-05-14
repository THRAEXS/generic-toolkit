package org.thraex.toolkit.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * @author 鬼王
 * @date 2022/05/15 00:29
 */
@ConfigurationProperties("thraex.security")
public class SecurityProperties {

    private Set<String> permitted = Collections.EMPTY_SET;

    public Set<String> getPermitted() {
        return permitted;
    }

    public SecurityProperties setPermitted(Set<String> permitted) {
        this.permitted = permitted;
        return this;
    }

}
