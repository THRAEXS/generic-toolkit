package org.thraex.toolkit.security.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.thraex.toolkit.security.user.TransitionalUser;
import reactor.core.publisher.Mono;

/**
 * TODO: Optimization
 *
 * @author 鬼王
 * @date 2022/04/26 18:03
 */
public abstract class ReactiveSecurityHolder {

    public static Mono<Authentication> authentication() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication);
    }

    public static Mono<String> username() {
        return authentication().map(Authentication::getName);
    }

    public static Mono<Object> principal() {
        return authentication().map(Authentication::getPrincipal);
    }

    public static Mono<UserDetails> userDetails() {
        return principal().map(p -> (UserDetails) p);
    }

    public static Mono<String> id() {
        return userDetails().map(u -> (TransitionalUser) u).map(TransitionalUser::getId);
    }

}
