package org.thraex.toolkit.exception;

import org.springframework.core.Ordered;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.handler.ExceptionHandlingWebHandler;
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
 * <br>
 * {@link ExceptionHandlingWebHandler}
 *
 * @author 鬼王
 * @date 2022/04/18 17:37
 */
public class HandlerFunctionExceptionHandler implements WebExceptionHandler, Ordered {

    private final Logger logger = Loggers.getLogger(getClass());

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
        if (ErrorResponse.class.isAssignableFrom(ex.getClass())) {
            return Mono.error(ex);
        }

        final Class<? extends Throwable> exception = ex.getClass();
        logger.warn("{}: [{}]", exception.getSimpleName(), ex.getMessage());

        return ServerResponse.ok()
                .bodyValue(EXCEPTION_TYPES.getOrDefault(exception, DEFAULT_EXCEPTION).apply(ex))
                .flatMap(response -> response.writeTo(exchange, new Context()));
    }

    @Override
    public int getOrder() {
        return -2;
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
