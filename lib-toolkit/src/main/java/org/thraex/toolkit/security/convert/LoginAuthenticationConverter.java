package org.thraex.toolkit.security.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import org.thraex.toolkit.response.ResponseStatus;
import org.thraex.toolkit.util.RSAUtil;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * TODO: Opt Exception
 *
 * @author 鬼王
 * @date 2022/03/22 18:44
 * @deprecated use
 * {@link HybridAuthenticationConverter}
 * instead.
 */
@Deprecated
public class LoginAuthenticationConverter implements ServerAuthenticationConverter {

    private final Logger logger = Loggers.getLogger(getClass());

    private final String prefix;

    private final String privateKey;

    public LoginAuthenticationConverter(String prefix, String privateKey) {
        this.prefix = prefix;
        this.privateKey = privateKey;
    }

    public static LoginAuthenticationConverter of(String prefix, String privateKey) {
        return new LoginAuthenticationConverter(prefix, privateKey);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        return Mono.justOrEmpty(headers.getFirst(HttpHeaders.AUTHORIZATION))
                .filter(StringUtils::isNotBlank)
                .flatMap(this::apply)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException(ResponseStatus.AUTHENTICATION_BAD_CREDENTIALS.phrase()))));
    }

    private Mono<Authentication> apply(String authorization) {
        logger.info("Authorization: [{}]", authorization);

        if (!StringUtils.startsWithIgnoreCase(authorization, prefix)) {
            return Mono.empty();
        }

        int length = prefix.length();
        String token = authorization.length() <= length ? "" : authorization.substring(length);
        if (StringUtils.isBlank(token)) {
            return Mono.empty();
        }

        try {
            String decrypted = RSAUtil.decrypt(token, privateKey);
            ObjectMapper mapper = new ObjectMapper();
            Params params = mapper.readValue(decrypted, Params.class);

            return Mono.just(new UsernamePasswordAuthenticationToken(params.getUsername(), params.getPassword()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return Mono.empty();
    }

    private static class Params {

        private String username;

        private String password;

        private String code;

        public String getUsername() {
            return username;
        }

        public Params setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Params setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getCode() {
            return code;
        }

        public Params setCode(String code) {
            this.code = code;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

    }

}
