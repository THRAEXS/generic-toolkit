package org.thraex.toolkit.security.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ServerWebExchange;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;
import org.thraex.toolkit.security.writer.ServerHttpResponseWriter;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/04/25 21:26
 */
public class ResponseStatusExceptionHandler {

    public static Mono<Void> unauthorized(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.set(HttpHeaders.WWW_AUTHENTICATE, String.format("Bearer error=\"%s\"", e.getMessage()));

        ResponseResult result = ResponseResult.fail(ResponseStatus.AUTHENTICATION_UNAUTHORIZED);

        return ServerHttpResponseWriter.write(response, HttpStatus.UNAUTHORIZED, result);
    }

    public static Mono<Void> denied(ServerWebExchange exchange, AccessDeniedException e) {
        ResponseResult result = ResponseResult.fail(ResponseStatus.AUTHENTICATION_ACCESS_DENIED);

        return ServerHttpResponseWriter.write(exchange, HttpStatus.FORBIDDEN, result);
    }

}
