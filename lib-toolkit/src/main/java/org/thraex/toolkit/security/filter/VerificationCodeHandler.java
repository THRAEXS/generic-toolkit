package org.thraex.toolkit.security.filter;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * @author 鬼王
 * @date 2022/05/14 19:35
 */
public interface VerificationCodeHandler {

    default Mono<Object> parse(Publisher<DataBuffer> body, Class<?> clazz) {
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
        ResolvableType elementType = ResolvableType.forClass(clazz);

        return decoder.decodeToMono(body, elementType, MediaType.APPLICATION_JSON, Collections.EMPTY_MAP);
    }

    Mono<Integer> convert(Flux<DataBuffer> body);

    boolean verify(Integer code);

}
