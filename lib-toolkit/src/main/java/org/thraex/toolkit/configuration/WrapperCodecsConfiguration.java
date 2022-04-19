package org.thraex.toolkit.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.thraex.toolkit.response.ResponseResult;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 鬼王
 * @date 2022/04/17 17:20
 */
public class WrapperCodecsConfiguration {

    @Bean
    CodecCustomizer wrapperCodecCustomizer(ObjectMapper objectMapper) {
        return configurer -> configurer.customCodecs().registerWithDefaultConfig(new WrapperHttpMessageWriter(objectMapper));
    }

    record WrapperHttpMessageWriter(ObjectMapper objectMapper) implements HttpMessageWriter {

        private static final Logger logger = Loggers.getLogger(WrapperHttpMessageWriter.class);

        private static final List<MediaType> MEDIA_TYPES = Collections.singletonList(MediaType.APPLICATION_JSON);

        @Override
        public List<MediaType> getWritableMediaTypes() {
            return MEDIA_TYPES;
        }

        @Override
        public boolean canWrite(ResolvableType elementType, MediaType mediaType) {
            return mediaType == null || MEDIA_TYPES.contains(mediaType);
        }

        @Override
        public Mono<Void> write(Publisher inputStream, ResolvableType actualType, ResolvableType elementType,
                                MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response, Map hints) {
            /**
             * Fix: Compatible with {@link org.springframework.mock.http.server.reactive.MockServerHttpResponse}
             */
            final HttpStatus status = response.getStatusCode();
            final Publisher is = status == null || status.is2xxSuccessful() ? wrapping(inputStream) : inputStream;
            return HttpMessageWriter.super.write(is, actualType, elementType, mediaType, request, response, hints);
        }

        @Override
        public Mono<Void> write(Publisher inputStream, ResolvableType elementType,
                                MediaType mediaType, ReactiveHttpOutputMessage message, Map hints) {
            return Mono.from(inputStream).flatMap(data -> {
                final ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(mapper(data));
                final DataBuffer buffer = message.bufferFactory().wrap(byteBuffer);

                final HttpHeaders headers = message.getHeaders();
                headers.setContentLength(byteBuffer.remaining());
                headers.setContentType(MediaType.APPLICATION_JSON);

                return message.writeWith(Mono.just(buffer));
            });
        }

        private Publisher wrapping(Publisher publisher) {
            return Mono.from(publisher).map(data -> ResponseResult.class.isInstance(data) ? data : ResponseResult.ok(data));
        }

        private String mapper(Object value) {
            String result;
            try {
                result = objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                final String error = e.getMessage();
                logger.error(error);
                result = error;
            }

            return result;
        }

    }

}
