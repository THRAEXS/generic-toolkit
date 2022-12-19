package org.thraex.dmpp.generic.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;
import org.thraex.toolkit.security.convert.HybridAuthenticationConverter;
import org.thraex.toolkit.security.filter.HybridAuthenticationWebFilter;
import org.thraex.toolkit.security.filter.TokenAuthenticationWebFilter;
import org.thraex.toolkit.security.filter.VerificationCodeHandler;
import org.thraex.toolkit.security.filter.VerificationCodeWebFilter;
import org.thraex.toolkit.security.handler.HybridAuthenticationSuccessHandler;
import org.thraex.toolkit.security.handler.ResponseStatusExceptionHandler;
import org.thraex.toolkit.security.manager.HybridReactiveAuthenticationManager;
import org.thraex.toolkit.security.properties.SecurityProperties;
import org.thraex.toolkit.security.token.TokenProcessor;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

/**
 * TODO: Minimal configuration
 *
 * {@link ReactiveUserDetailsServiceAutoConfiguration}
 *
 * @author 鬼王
 * @date 2022/03/18 16:47
 */
@Import(TokenProcessor.class)
@EnableConfigurationProperties(SecurityProperties.class)
public class WebFluxSecurityConfiguration {

    private SecurityProperties securityProperties;

    private TokenProcessor tokenProcessor;

    private ObjectMapper mapper;

    @Autowired(required = false)
    private VerificationCodeHandler verificationCodeHandler;

    public WebFluxSecurityConfiguration(SecurityProperties securityProperties,
                                        TokenProcessor tokenProcessor,
                                        ObjectMapper mapper) {
        this.securityProperties = securityProperties;
        this.tokenProcessor = tokenProcessor;
        this.mapper = mapper;
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(@Autowired(required = false) ReactiveUserDetailsService userDetailsService) {
        ReactiveUserDetailsService service = Optional.ofNullable(userDetailsService).orElse(username -> Mono.empty());
        HybridReactiveAuthenticationManager authenticationManager = new HybridReactiveAuthenticationManager(service);
        authenticationManager.setAuthenticationMethod(securityProperties.getAuthenticationMethod());
        Optional.ofNullable(verificationCodeHandler).ifPresent(authenticationManager::setVerificationCodeHandler);

        return authenticationManager;
    }

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
                                               ReactiveAuthenticationManager authenticationManager) {
        Set<String> permitted = securityProperties.getPermitted();

        ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange = http.authorizeExchange();
        if (!permitted.isEmpty()) {
            authorizeExchange.pathMatchers(permitted.toArray(new String[0])).permitAll();
        }
        authorizeExchange.anyExchange().authenticated();

        http.csrf().disable().headers().frameOptions().disable();

        SecurityWebFiltersOrder httpBasic = SecurityWebFiltersOrder.HTTP_BASIC;

        Optional.ofNullable(verificationCodeHandler).ifPresent(it ->
                http.addFilterBefore(new VerificationCodeWebFilter(verificationCodeHandler), httpBasic));

        http.addFilterAt(loginAuthenticationWebFilter(authenticationManager), httpBasic)
                .addFilterAt(TokenAuthenticationWebFilter.of(tokenProcessor), SecurityWebFiltersOrder.AUTHENTICATION);

        http.exceptionHandling()
                .authenticationEntryPoint(ResponseStatusExceptionHandler::unauthorized)
                .accessDeniedHandler(ResponseStatusExceptionHandler::denied);

        return http.build();
    }

    private WebFilter loginAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        return new HybridAuthenticationWebFilter(
                authenticationManager,
                HybridAuthenticationConverter.of(tokenProcessor.getPrefix(), tokenProcessor.getPrivateKey(), mapper),
                HybridAuthenticationSuccessHandler.of(this::converter)
        );
    }

    private String converter(Authentication authentication) {
        return tokenProcessor.generate(claims -> {
            claims.setSubject(authentication.getName());
            claims.setClaim("principal", authentication.getPrincipal());
        });
    }

}
