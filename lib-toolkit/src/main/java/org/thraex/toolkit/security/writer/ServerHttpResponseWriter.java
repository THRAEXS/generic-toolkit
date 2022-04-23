package org.thraex.toolkit.security.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;
import org.thraex.toolkit.response.ResponseResult;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/04/24 02:00
 */
public abstract class ServerHttpResponseWriter {

    public static Mono<Void> ok(WebFilterExchange exchange, ResponseResult<String> data) {
        return write(exchange.getExchange(), HttpStatus.OK, data);
    }

    public static Mono<Void> write(ServerWebExchange exchange, HttpStatus status, ResponseResult<String> data) {
        return write(exchange.getResponse(), status, data);
    }

    public static Mono<Void> write(ServerHttpResponse response, HttpStatus status, ResponseResult<String> data) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(status);

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = new byte[0];
        try {
            bytes = mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        DataBufferFactory bufferFactory = response.bufferFactory();
        DataBuffer wrap = bufferFactory.wrap(bytes);

        return response.writeWith(Mono.just(wrap));
    }

}
