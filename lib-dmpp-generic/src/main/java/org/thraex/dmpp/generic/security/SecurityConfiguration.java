package org.thraex.dmpp.generic.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;
import org.thraex.toolkit.security.token.TokenProcessor;
import org.thraex.toolkit.security.token.TokenProperties;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

/**
 * @author 鬼王
 * @date 2022/03/18 16:47
 */
@Import(TokenProperties.class)
@ConfigurationProperties("thraex.security")
@EnableConfigurationProperties(TokenProperties.class)
public class SecurityConfiguration {

    private Set<String> permitted = Collections.EMPTY_SET;

    private String prefix;

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
                                               ReactiveAuthenticationManager manager,
                                               TokenProcessor tokenProcessor) {
        this.prefix = tokenProcessor.getProperties().getPrefix();

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = http.authorizeExchange();
        if (!permitted.isEmpty()) {
            authorizeExchange.pathMatchers(permitted.toArray(new String[0])).permitAll();
        }
        authorizeExchange.anyExchange().authenticated();

        http.csrf().disable().headers().frameOptions().disable();

        http.authenticationManager(manager)
                .addFilterAt(LoginAuthenticationWebFilter.of(manager, tokenProcessor), SecurityWebFiltersOrder.HTTP_BASIC)
                .addFilterAt(TokenAuthenticationWebFilter.of(tokenProcessor), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling()
                .authenticationEntryPoint(this::unauthorized)
                .accessDeniedHandler(this::denied);

        return http.build();
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("%s error=\"%s\"", prefix, e.getMessage()));

        return ServerHttpResponseWriter.write(response, HttpStatus.UNAUTHORIZED,
                ResponseResult.fail(ResponseStatus.AUTHENTICATION_UNAUTHORIZED));
    }

    private Mono<Void> denied(ServerWebExchange exchange, AccessDeniedException e) {
        return ServerHttpResponseWriter.write(exchange, HttpStatus.FORBIDDEN,
                ResponseResult.fail(ResponseStatus.AUTHENTICATION_ACCESS_DENIED));
    }

    public void setPermitted(Set<String> permitted) {
        this.permitted = permitted;
    }

}
