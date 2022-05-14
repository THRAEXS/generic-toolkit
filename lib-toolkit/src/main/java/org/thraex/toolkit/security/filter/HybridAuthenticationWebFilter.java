package org.thraex.toolkit.security.filter;

import org.springframework.http.HttpMethod;
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
import org.thraex.toolkit.security.constant.AuthenticationMethod;
import org.thraex.toolkit.security.convert.LoginAuthenticationConverter;
import org.thraex.toolkit.security.handler.LoginAuthenticationFailureHandler;
import org.thraex.toolkit.security.handler.LoginAuthenticationSuccessHandler;
import org.thraex.toolkit.security.token.TokenProcessor;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/05/15 01:15
 */
public class HybridAuthenticationWebFilter implements WebFilter {

    private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login");

    private ReactiveAuthenticationManager authenticationManager;

    private AuthenticationMethod authenticationMethod;

    private ServerAuthenticationConverter authenticationConverter;

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler;

    private ServerAuthenticationFailureHandler authenticationFailureHandler = LoginAuthenticationFailureHandler.of();

    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    public HybridAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager,
                                         AuthenticationMethod authenticationMethod,
                                         TokenProcessor tokenProcessor,
                                         String prefix,
                                         String privateKey) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(authenticationMethod, "authenticationMethod cannot be null");

        this.authenticationManager = authenticationManager;
        this.authenticationMethod = authenticationMethod;
        this.authenticationConverter = LoginAuthenticationConverter.of(prefix, privateKey);

        this.authenticationSuccessHandler = LoginAuthenticationSuccessHandler.of(tokenProcessor);
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
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    public HybridAuthenticationWebFilter setAuthenticationManager(ReactiveAuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationManager = authenticationManager;
        return this;
    }

    public HybridAuthenticationWebFilter setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        Assert.notNull(authenticationMethod, "authenticationMethod cannot be null");
        this.authenticationMethod = authenticationMethod;
        return this;
    }

    public HybridAuthenticationWebFilter setAuthenticationConverter(ServerAuthenticationConverter authenticationConverter) {
        Assert.notNull(authenticationConverter, "authenticationConverter cannot be null");
        this.authenticationConverter = authenticationConverter;
        return this;
    }

    public HybridAuthenticationWebFilter setAuthenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }

    public HybridAuthenticationWebFilter setAuthenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
        this.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

}
