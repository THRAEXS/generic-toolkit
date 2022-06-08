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
    private final String code;

    public HybridAuthenticationToken(Object principal, Object credentials, String code) {
        super(principal, credentials);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
