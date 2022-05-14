package org.thraex.dmpp.generic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.thraex.toolkit.security.filter.LoginAuthenticationWebFilter;
import org.thraex.toolkit.security.filter.TokenAuthenticationWebFilter;
import org.thraex.toolkit.security.filter.VerificationCodeHandler;
import org.thraex.toolkit.security.filter.VerificationCodeWebFilter;
import org.thraex.toolkit.security.handler.ResponseStatusExceptionHandler;
import org.thraex.toolkit.security.token.TokenProcessor;
import org.thraex.toolkit.security.token.TokenProperties;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * {@link ReactiveUserDetailsServiceAutoConfiguration}
 *
 * @author 鬼王
 * @date 2022/03/18 16:47
 */
@ConditionalOnBean(ReactiveUserDetailsService.class)
@ConfigurationProperties("thraex.security")
@Import(TokenProcessor.class)
@EnableConfigurationProperties(TokenProperties.class)
public class WebFluxSecurityConfiguration {

    private Set<String> permitted = Collections.EMPTY_SET;

    @Autowired(required = false)
    private VerificationCodeHandler verificationCodeHandler;

    @Bean
    ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService service) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(service);
    }

    @Bean
    SecurityWebFilterChain securityFilterChain(ApplicationContext context,
                                               ServerHttpSecurity http,
                                               ReactiveAuthenticationManager manager,
                                               TokenProcessor tokenProcessor) {
        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = http.authorizeExchange();
        if (!permitted.isEmpty()) {
            authorizeExchange.pathMatchers(permitted.toArray(new String[0])).permitAll();
        }
        authorizeExchange.anyExchange().authenticated();

        http.csrf().disable().headers().frameOptions().disable();

        SecurityWebFiltersOrder httpBasic = SecurityWebFiltersOrder.HTTP_BASIC;

        Optional.ofNullable(verificationCodeHandler).ifPresent(it ->
                http.addFilterBefore(new VerificationCodeWebFilter(verificationCodeHandler), httpBasic));

        http.addFilterAt(LoginAuthenticationWebFilter.of(manager, tokenProcessor), httpBasic)
                .addFilterAt(TokenAuthenticationWebFilter.of(tokenProcessor), SecurityWebFiltersOrder.AUTHENTICATION);

        http.exceptionHandling()
                .authenticationEntryPoint(ResponseStatusExceptionHandler::unauthorized)
                .accessDeniedHandler(ResponseStatusExceptionHandler::denied);

        return http.build();
    }

    public void setPermitted(Set<String> permitted) {
        this.permitted = permitted;
    }

}
