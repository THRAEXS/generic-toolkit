package org.thraex.toolkit.security.filter;

import io.netty.handler.codec.http.HttpMethod;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.thraex.toolkit.security.token.TokenProcessor;
import org.thraex.toolkit.security.token.TokenProperties;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/03/21 22:18
 */
public class LoginAuthenticationWebFilter implements WebFilter {

    private final ReactiveAuthenticationManager authenticationManager;

    private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login");

    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    private ServerAuthenticationConverter authenticationConverter;

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler;

    private ServerAuthenticationFailureHandler authenticationFailureHandler;

    public LoginAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager, TokenProcessor tokenProcessor) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(tokenProcessor, "tokenProcessor cannot be null");

        this.authenticationManager = authenticationManager;

        TokenProperties properties = tokenProcessor.getProperties();
        this.authenticationConverter = LoginAuthenticationConverter.of(properties.getPrefix(), properties.getPrivateKey());
        this.authenticationSuccessHandler = LoginAuthenticationSuccessHandler.of(tokenProcessor);
        this.authenticationFailureHandler = LoginAuthenticationFailureHandler.of();
    }

    public static LoginAuthenticationWebFilter of(ReactiveAuthenticationManager authenticationManager, TokenProcessor tokenProcessor) {
        return new LoginAuthenticationWebFilter(authenticationManager, tokenProcessor);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        WebFilterExchange filterExchange = new WebFilterExchange(exchange, chain);

        return matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> authenticationConverter.convert(exchange))
                .flatMap(token -> authenticate(filterExchange, token))
                .onErrorResume(AuthenticationException.class,
                        e -> authenticationFailureHandler.onAuthenticationFailure(filterExchange, e));
    }

    private Mono<Void> authenticate(WebFilterExchange exchange, Authentication token) {
        return authenticationManager.authenticate(token)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ProviderNotFoundException("No provider found for " + token.getClass()))))
                .flatMap(authentication -> onAuthenticationSuccess(exchange, authentication));
    }

    protected Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);

        return securityContextRepository.save(exchange.getExchange(), securityContext)
                .then(authenticationSuccessHandler.onAuthenticationSuccess(exchange, authentication))
                .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

}
