package org.thraex.toolkit.security.filter;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * https://github.com/spring-projects/spring-security/issues/5931
 * https://linuxtut.com/en/bb48bd7604f335c6d553/
 *
 * @author 鬼王
 * @date 2022/05/13 15:53
 */
public class VerificationCodeWebFilter implements WebFilter {

    private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/verification/code/send");

    private final VerificationCodeHandler handler;

    public VerificationCodeWebFilter(VerificationCodeHandler handler) {
        this.handler = handler;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> convert(exchange))
                .flatMap(it -> ServerHttpResponseWriter.ok(exchange, ResponseResult.ok(it)))
                .onErrorResume(Exception.class, e -> Mono.error(new IllegalArgumentException(e.getMessage())));
    }

    private Mono<Integer> convert(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("Parameter missing"))))
                .map(Flux::just)
                .next()
                .flatMap(handler::convert);
    }

}
