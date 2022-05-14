package org.thraex.toolkit.security.filter;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;


/**
 * https://github.com/spring-projects/spring-security/issues/5931
 * https://linuxtut.com/en/bb48bd7604f335c6d553/
 *
 * @author 鬼王
 * @date 2022/05/13 15:53
 */
public class VerificationCodeWebFilter implements WebFilter {

    private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/verification/code/send");

//    private final VerificationCodeHandler handler;

//    public VerificationCodeWebFilter(VerificationCodeHandler handler) {
//        this.handler = handler;
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Flux<DataBuffer> body1 = exchange.getRequest().getBody();
//        Flux<Flux<DataBuffer>> map = body1.map(Flux::just);
        Flux<Mono<DataBuffer>> map1 = body1.map(Mono::just);
        Flux<Mono<Params>> map2 = map1.map(body -> {
            Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
            ResolvableType elementType = ResolvableType.forClass(Params.class);
            return decoder.decodeToMono(body, elementType,
                    MediaType.APPLICATION_JSON, Collections.EMPTY_MAP).cast(Params.class);
        });
        Mono<Mono<Params>> next = map2.next();
        Mono<Params> mono = next.flatMap(it -> it);

        return mono.flatMap(it -> ServerHttpResponseWriter.ok(exchange, ResponseResult.ok(it)));


        /*matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .map(matchResult -> exchange.getRequest().getBody())
                .map(body -> {
                    Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
                    ResolvableType elementType = ResolvableType.forClass(Params.class);
                    return decoder.decodeToMono(body, elementType,
                            MediaType.APPLICATION_JSON, Collections.EMPTY_MAP).cast(Params.class);
                });*/

//        return matcher.matches(exchange)
//                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
//                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
//                .flatMap(matchResult -> exchange.getRequest().getBody())
    }

    public Mono<Void> filter1(ServerWebExchange exchange, WebFilterChain chain) {
        Mono<Params> objectMono = matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> exchange.getRequest().getBody()
                        .map(Flux::just)
                        .map(body -> {
                            Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
                            ResolvableType elementType = ResolvableType.forClass(Params.class);
                            return decoder.decodeToMono(body, elementType,
                                    MediaType.APPLICATION_JSON, Collections.EMPTY_MAP).cast(Params.class);
                        }).next()
                        .flatMap(it -> it)
                );

        return objectMono.flatMap(it -> {
            return ServerHttpResponseWriter.ok(exchange, ResponseResult.ok(it));
        });

//        return matcher.matches(exchange)
//                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
//                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
//                .map(matchResult -> exchange.getRequest().getBody());
    }

    public static class Params {

        private String mobile;

        private String method;

        public String getMobile() {
            return mobile + "-gui";
        }

        public Params setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public String getMethod() {
            return method;
        }

        public Params setMethod(String method) {
            this.method = method;
            return this;
        }
    }

}
