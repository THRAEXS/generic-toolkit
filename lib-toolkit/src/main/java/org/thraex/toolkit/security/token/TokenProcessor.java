package org.thraex.toolkit.security.token;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.Assert;
import org.thraex.toolkit.util.RSAUtil;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Set;
import java.util.function.Consumer;

/**
 * TODO: Opt
 *
 * @author 鬼王
 * @date 2022/03/29 10:37
 */
@EnableConfigurationProperties(TokenProperties.class)
public class TokenProcessor {

    private Logger logger = Loggers.getLogger(TokenProcessor.class);

    private final TokenProperties properties;

    private final RsaJsonWebKey rsaJsonWebKey;

    private final JwtConsumer consumer;

    public TokenProcessor(TokenProperties properties) throws JoseException, GeneralSecurityException {
        Assert.notNull(properties, "properties cannot be null");

        this.properties = properties;
        this.rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId(properties.getKeyId());

        KeyPair keyPair = RSAUtil.parser(properties.getPublicKey(), properties.getPrivateKey());
        rsaJsonWebKey.setPrivateKey(keyPair.getPrivate());

        JwtConsumerBuilder builder = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(properties.getIss())
                .setVerificationKey(keyPair.getPublic())
                .setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                        AlgorithmIdentifiers.RSA_USING_SHA256);

        Set<String> aud = properties.getAud();
        if (!aud.isEmpty()) {
            builder.setExpectedAudience(aud.toArray(new String[0]));
        }

        this.consumer = builder.build();
    }

    public JwtClaims claims() {
        JwtClaims claims = new JwtClaims();

        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setIssuer(properties.getIss());
        claims.setExpirationTimeMinutesInTheFuture(properties.getExp());
        claims.setNotBeforeMinutesInThePast(properties.getNbf());

        Set<String> aud = properties.getAud();
        if (!aud.isEmpty()) {
            claims.setAudience(aud.toArray(new String[0]));
        }

        return claims;
    }

    public String generate(JwtClaims claims) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = null;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            logger.error(e.getMessage());
        }

        return String.format("%s%s", properties.getPrefix(), jwt);
    }

    public String generate(Consumer<JwtClaims> consumer) {
        Assert.notNull(consumer, "consumer cannot be null");

        JwtClaims claims = claims();
        consumer.accept(claims);

        return generate(claims);
    }

    public JwtClaims verify(String token) throws InvalidJwtException {
        Assert.notNull(token, "token cannot be null");

        String prefix = properties.getPrefix();
        String jwt = token.length() <= prefix.length() ? "" : token.substring(prefix.length());

        return consumer.processToClaims(jwt);
    }

    public TokenProperties getProperties() {
        return properties;
    }

    public String getPrefix() {
        return properties.getPrefix();
    }

    public String getPrivateKey() {
        return properties.getPrivateKey();
    }

    public String getPublicKey() {
        return properties.getPublicKey();
    }

}
