package org.thraex.toolkit.security.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import org.thraex.toolkit.security.token.TokenProcessor;
import org.thraex.toolkit.security.user.TransitionalUser;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * TODO: Opt Exception
 *
 * @author 鬼王
 * @date 2022/03/25 14:03
 */
public class TokenAuthenticationConverter implements ServerAuthenticationConverter {

    private final Logger logger = Loggers.getLogger(getClass());

    private TokenProcessor tokenProcessor;
    private final String prefix;

    public TokenAuthenticationConverter(TokenProcessor tokenProcessor) {
        this.tokenProcessor = tokenProcessor;
        this.prefix = tokenProcessor.getProperties().getPrefix();
    }

    public static TokenAuthenticationConverter of(TokenProcessor tokenProcessor) {
        return new TokenAuthenticationConverter(tokenProcessor);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .log(logger, Level.INFO, false, SignalType.ON_SUBSCRIBE, SignalType.ON_NEXT)
                .filter(a -> StringUtils.startsWithIgnoreCase(a, prefix))
                .flatMap(this::apply);
    }

    private Mono<Authentication> apply(String authorization) {
        try {
            JwtClaims claims = tokenProcessor.verify(authorization);
            logger.debug(claims.toString());

            ObjectMapper mapper = new ObjectMapper();
            String principalString = claims.getClaimValueAsString("principal");
            UserDetails principal = TransitionalUser.withMap(mapper.readValue(principalString, Map.class)).build();

            return Mono.just(new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities()));
        } catch (InvalidJwtException | IOException e) {
            logger.error(e.getMessage());
        }

        return Mono.empty();
    }

}
