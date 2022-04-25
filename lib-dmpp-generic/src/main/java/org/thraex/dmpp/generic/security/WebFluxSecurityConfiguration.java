package org.thraex.dmpp.generic.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.thraex.toolkit.security.filter.LoginAuthenticationWebFilter;
import org.thraex.toolkit.security.filter.TokenAuthenticationWebFilter;
import org.thraex.toolkit.security.handler.ResponseStatusExceptionHandler;
import org.thraex.toolkit.security.token.TokenProcessor;
import org.thraex.toolkit.security.token.TokenProperties;

import java.util.Collections;
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
@EnableWebFluxSecurity
public class WebFluxSecurityConfiguration {

    private Set<String> permitted = Collections.EMPTY_SET;

    @Bean
    ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService service) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(service);
    }

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
                                               ReactiveAuthenticationManager manager,
                                               TokenProcessor tokenProcessor) {
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
                .authenticationEntryPoint(ResponseStatusExceptionHandler::unauthorized)
                .accessDeniedHandler(ResponseStatusExceptionHandler::denied);

        return http.build();
    }

    public void setPermitted(Set<String> permitted) {
        this.permitted = permitted;
    }

}
