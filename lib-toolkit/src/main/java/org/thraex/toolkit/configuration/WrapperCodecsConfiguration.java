package org.thraex.toolkit.configuration;

import org.reactivestreams.Publisher;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.thraex.toolkit.response.ResponseResult;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
    CodecCustomizer wrapperCodecCustomizer() {
        return configurer -> configurer.customCodecs().registerWithDefaultConfig(new WrapperHttpMessageWriter());
    }


    static class WrapperHttpMessageWriter implements HttpMessageWriter {

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
        public Mono<Void> write(Publisher inputStream, ResolvableType elementType,
                                MediaType mediaType, ReactiveHttpOutputMessage message, Map hints) {
            Charset charset = StandardCharsets.UTF_8;

            return Mono.from(inputStream).flatMap(data -> {
                final Object result = data instanceof ResponseResult ? data : ResponseResult.ok(data);

                final ByteBuffer byteBuffer = charset.encode(result.toString());
                final DataBuffer buffer = message.bufferFactory().wrap(byteBuffer);

                final HttpHeaders headers = message.getHeaders();
                headers.setContentLength(byteBuffer.remaining());
                headers.setContentType(MediaType.APPLICATION_JSON);

                return message.writeWith(Mono.just(buffer));
            });
        }
    }

}
