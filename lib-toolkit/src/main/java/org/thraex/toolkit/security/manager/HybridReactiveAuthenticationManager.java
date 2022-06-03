package org.thraex.toolkit.security.manager;

import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.thraex.toolkit.security.authentication.AuthenticationMethod;
import org.thraex.toolkit.security.authentication.HybridAuthenticationToken;
import org.thraex.toolkit.security.filter.NoOpVerificationCodeHandler;
import org.thraex.toolkit.security.filter.VerificationCodeHandler;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * TODO: Optimization
 *
 * @author 鬼王
 * @date 2022/06/03 16:15
 */
public class HybridReactiveAuthenticationManager extends AbstractUserDetailsReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private Scheduler scheduler = Schedulers.boundedElastic();

    private AuthenticationMethod authenticationMethod = AuthenticationMethod.USERNAME_PASSWORD;

    private VerificationCodeHandler verificationCodeHandler = NoOpVerificationCodeHandler.getInstance();

    private final Map<AuthenticationMethod, Function<Authentication, Mono<Authentication>>> AUTHENTICATE = Map.of(
            AuthenticationMethod.USERNAME_PASSWORD, authentication -> super.authenticate(authentication),
            AuthenticationMethod.VERIFICATION_CODE, authentication -> authenticate(authentication, false),
            AuthenticationMethod.TWO_FACTOR, authentication -> authenticate(authentication, true)
    );

    public HybridReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return AUTHENTICATE.get(authenticationMethod).apply(authentication);
    }

    private Mono<Authentication> authenticate(Authentication authentication, boolean twoFactor) {
        String username = authentication.getName();
        String presentedPassword = Optional.ofNullable(authentication.getCredentials()).map(p -> p.toString()).orElse("");

        return Mono.just(authentication)
                .cast(HybridAuthenticationToken.class)
                .map(verificationCodeHandler::verify)
                .flatMap(valid -> valid ? retrieveUser(username) : Mono.empty())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Verification Code"))))
                .publishOn(this.scheduler)
                .filter(userDetails -> !twoFactor || (twoFactor && this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
                .map(this::createUsernamePasswordAuthenticationToken);
    }

    @Override
    protected Mono<UserDetails> retrieveUser(String username) {
        return userDetailsService.findByUsername(username);
    }

    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(UserDetails userDetails) {
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    public void setScheduler(Scheduler scheduler) {
        Assert.notNull(scheduler, "scheduler cannot be null");
        this.scheduler = scheduler;
    }

    public HybridReactiveAuthenticationManager setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        Assert.notNull(authenticationMethod, "authenticationMethod cannot be null");
        this.authenticationMethod = authenticationMethod;
        return this;
    }

    public HybridReactiveAuthenticationManager setVerificationCodeHandler(VerificationCodeHandler verificationCodeHandler) {
        Assert.notNull(verificationCodeHandler, "verificationCodeHandler cannot be null");
        this.verificationCodeHandler = verificationCodeHandler;
        return this;
    }

}
