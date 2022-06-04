package org.thraex.toolkit.security.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.thraex.toolkit.security.authentication.HybridAuthenticationToken;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 鬼王
 * @date 2022/06/03 18:23
 */
public class NoOpVerificationCodeHandler implements VerificationCodeHandler {

    private static final NoOpVerificationCodeHandler INSTANCE = new NoOpVerificationCodeHandler();

    @Override
    public Mono<Integer> convert(Flux<DataBuffer> body) {
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> send(Flux<DataBuffer> body) {
        return Mono.empty();
    }

    @Override
    public boolean verify(HybridAuthenticationToken authentication) {
        return false;
    }

    public static NoOpVerificationCodeHandler getInstance() {
        return INSTANCE;
    }

}
