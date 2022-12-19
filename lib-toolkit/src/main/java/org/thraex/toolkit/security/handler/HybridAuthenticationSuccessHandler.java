package org.thraex.toolkit.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.util.Assert;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.function.Function;

/**
 * @author 鬼王
 * @date 2022/05/15 01:42
 */
public class HybridAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final Logger logger = Loggers.getLogger(getClass());

    private final Function<Authentication, String> converter;

    public HybridAuthenticationSuccessHandler(Function<Authentication, String> converter) {
        Assert.notNull(converter, "converter cannot be null");
        this.converter = converter;
    }

    public static HybridAuthenticationSuccessHandler of(Function<Authentication, String> converter) {
        return new HybridAuthenticationSuccessHandler(converter);
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        logger.info("Authentication success: [{}]", authentication.getName());

        return ServerHttpResponseWriter.ok(exchange, ResponseResult.ok(converter.apply(authentication)));
    }

}
