package org.thraex.toolkit.security.exception;

import org.springframework.security.core.AuthenticationException;
import org.thraex.toolkit.response.ResponseStatus;

/**
 * @author 鬼王
 * @date 2022/03/30 18:40
 */
@Deprecated
public class TokenAuthenticationException extends AuthenticationException {

    private ResponseStatus status;

    public TokenAuthenticationException(ResponseStatus status) {
        super(status.phrase());
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public static TokenAuthenticationException of(ResponseStatus status) {
        return new TokenAuthenticationException(status);
    }

}
