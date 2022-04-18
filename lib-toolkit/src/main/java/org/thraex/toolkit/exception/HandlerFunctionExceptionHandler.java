package org.thraex.toolkit.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.thraex.toolkit.response.ResponseResult;
import org.thraex.toolkit.response.ResponseStatus;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * TODO: Optimization global Exception
 *
 * @author 鬼王
 * @date 2022/04/18 17:37
 */
public class HandlerFunctionExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger logger = Loggers.getLogger(HandlerFunctionExceptionHandler.class);

    private static final Function<Throwable, Object> DEFAULT_EXCEPTION = e -> ResponseResult.fail(e.getMessage());

    private static final Map<Class<?>, Function<Throwable, Object>> EXCEPTION_TYPES = Map.of(
            EmptyResultDataAccessException.class, ex -> ResponseResult.fail(ResponseStatus.TARGET_NOT_EXIST),
            IllegalArgumentException.class, ex -> ResponseResult.fail(ResponseStatus.ILLEGAL_ARGUMENT, ex.getMessage())
    );

    private final List<HttpMessageWriter<?>> messageWriters;

    public HandlerFunctionExceptionHandler(ServerCodecConfigurer serverCodecConfigurer) {
        this.messageWriters = serverCodecConfigurer.getWriters();
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        final Class<? extends Throwable> exception = ex.getClass();
        logger.warn("{}: [{}]", exception.getSimpleName(), ex.getMessage());

        return ServerResponse.ok()
                .bodyValue(EXCEPTION_TYPES.getOrDefault(exception, DEFAULT_EXCEPTION).apply(ex))
                .flatMap(response -> response.writeTo(exchange, new Context()));
    }

    private class Context implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return Collections.emptyList();
        }

    }

}
