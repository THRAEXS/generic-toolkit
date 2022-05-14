package org.thraex.toolkit.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author 鬼王
 * @date 2022/05/15 03:19
 */
public class HybridAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * Verification code
     */
    private final Integer code;

    public HybridAuthenticationToken(Object principal, Object credentials, Integer code) {
        super(principal, credentials);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
