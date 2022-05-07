package org.thraex.toolkit.security.handler;

import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * @author 鬼王
 * @date 2022/03/25 16:48
 */
public class LoginAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    private final Logger logger = Loggers.getLogger(getClass());

    public static LoginAuthenticationFailureHandler of() {
        return new LoginAuthenticationFailureHandler();
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange exchange, AuthenticationException exception) {
        String message = exception.getMessage();
        logger.warn("Authentication failure: [{}]", message);

        ResponseStatus status = exception instanceof ProviderNotFoundException ?
                ResponseStatus.INTERNAL_SERVER_ERROR :
                ResponseStatus.AUTHENTICATION_BAD_CREDENTIALS;

        return ServerHttpResponseWriter.ok(exchange, ResponseResult.fail(status, message));
    }

}
