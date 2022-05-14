package org.thraex.toolkit.security.filter;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.thraex.toolkit.security.handler.LoginAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/05/15 01:15
 */
public class HybridAuthenticationWebFilter implements WebFilter {

    private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login");

    private ReactiveAuthenticationManager authenticationManager;

    private ServerAuthenticationConverter authenticationConverter;

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler;

    private ServerAuthenticationFailureHandler authenticationFailureHandler = LoginAuthenticationFailureHandler.of();

    public HybridAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager,
                                         ServerAuthenticationConverter authenticationConverter,
                                         ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
        Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");

        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        WebFilterExchange filterExchange = new WebFilterExchange(exchange, chain);

        return matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> authenticationConverter.convert(exchange))
                .map(authentication -> authentication)
                .flatMap(authentication -> authenticate(filterExchange, authentication))
                .onErrorResume(AuthenticationException.class,
                        e -> authenticationFailureHandler.onAuthenticationFailure(filterExchange, e));
    }

    private Mono<Void> authenticate(WebFilterExchange exchange, Authentication authentication) {
        return authenticationManager.authenticate(authentication)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ProviderNotFoundException("No provider found for " + authentication.getClass()))))
                .flatMap(auth -> authenticationSuccessHandler.onAuthenticationSuccess(exchange, auth));
    }

    public HybridAuthenticationWebFilter setAuthenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

}
