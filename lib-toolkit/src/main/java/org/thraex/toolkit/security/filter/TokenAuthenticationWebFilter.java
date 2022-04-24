package org.thraex.toolkit.security.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;
import org.thraex.toolkit.security.convert.TokenAuthenticationConverter;
import org.thraex.toolkit.security.exception.TokenAuthenticationException;
import org.thraex.toolkit.security.token.TokenProcessor;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Optional;

/**
 * @author 鬼王
 * @date 2022/03/28 20:07
 */
public class TokenAuthenticationWebFilter implements WebFilter {

    private Logger logger = Loggers.getLogger(TokenAuthenticationWebFilter.class);

    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    private ServerAuthenticationConverter authenticationConverter;

    private final String prefix;

    public TokenAuthenticationWebFilter(TokenProcessor tokenProcessor) {
        Assert.notNull(tokenProcessor, "tokenProcessor cannot be null");

        this.authenticationConverter = TokenAuthenticationConverter.of(tokenProcessor);
        this.prefix = tokenProcessor.getProperties().getPrefix();
    }

    public static TokenAuthenticationWebFilter of(TokenProcessor tokenProcessor) {
        return new TokenAuthenticationWebFilter(tokenProcessor);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return authenticationConverter.convert(exchange)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(authentication -> onAuthenticationSuccess(exchange, chain, authentication));
    }

    protected Mono<Void> onAuthenticationSuccess(ServerWebExchange exchange,
                                                 WebFilterChain chain,
                                                 Authentication authentication) {
        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);

        return securityContextRepository.save(exchange, securityContext)
                .then(chain.filter(exchange))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    @Deprecated
    protected Mono<Void> onAuthenticationFailure(ServerWebExchange exchange, AuthenticationException exception) {
        String message = exception.getMessage();
        logger.info("Authentication(Token) failure: [{}]", message);

        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("%s error=\"%s\"", prefix, message));

        ResponseStatus status = null;
        if (exception instanceof TokenAuthenticationException) {
            TokenAuthenticationException e = (TokenAuthenticationException) exception;
            status = e.getStatus();
        }

        return ServerHttpResponseWriter.write(response, HttpStatus.UNAUTHORIZED,
                ResponseResult.fail(Optional.ofNullable(status).orElse(ResponseStatus.AUTHENTICATION_INVALID_TOKEN)));
    }

}
