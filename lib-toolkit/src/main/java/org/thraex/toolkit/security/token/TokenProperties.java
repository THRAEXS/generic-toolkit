package org.thraex.toolkit.security.token;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples
 *
 * @author 鬼王
 * @date 2022/03/28 22:05
 */
@ConfigurationProperties("thraex.security.token")
public class TokenProperties {

    private final String POWER = "THRAEX";

    /**
     * Give the JWK a Key ID (kid), which is just the polite thing to do
     */
    private String keyId = String.format("%s-KEY", POWER);

    /**
     * Who creates the token and signs it
     */
    private String iss = POWER;

    /**
     * To whom the token is intended to be sent
     */
    private Set<String> aud = Collections.EMPTY_SET;

    /**
     * time when the token will expire (120 minutes from now)
     */
    private float exp = 120;

    /**
     * time before which the token is not yet valid (2 minutes ago)
     */
    private float nbf = 2;

    /**
     * Bearer Authentication
     */
    private String prefix = "Bearer ";

    /**
     * RSA public key
     */
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgVt4gqPbRI6I+hx3bX9pOPQzpoRNXO+zA5el5gTmhOE6mpfJFmg+097/Msn879H/REZfn2GGT+jGug4WbR5gMKA2G/73M0gfK69Uo6vdS0zMeUeDreAsYG5appgBt3P5zziG/1HCzwjw4Wft57owhDVJFOpkO65n6gKiNR3q6pbelVm2UZcLCSJDF/tt4DhKdyaBwK0xFJ/wRGPKBVBmu1uJKN4KbIQQjhr52BelUdrMJAI+d53ZjNkudK4TVkQikmSpHHYGji/rQ1OKEjSEEanqoGy5oDfzP6Xr//oMMSEAPLFOweJEx77omRKUkmwp4RYKUfy0F1ln8OxAWNrQfQIDAQAB";

    /**
     * RSA private key
     */
    private String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCBW3iCo9tEjoj6HHdtf2k49DOmhE1c77MDl6XmBOaE4Tqal8kWaD7T3v8yyfzv0f9ERl+fYYZP6Ma6DhZtHmAwoDYb/vczSB8rr1Sjq91LTMx5R4Ot4CxgblqmmAG3c/nPOIb/UcLPCPDhZ+3nujCENUkU6mQ7rmfqAqI1Herqlt6VWbZRlwsJIkMX+23gOEp3JoHArTEUn/BEY8oFUGa7W4ko3gpshBCOGvnYF6VR2swkAj53ndmM2S50rhNWRCKSZKkcdgaOL+tDU4oSNIQRqeqgbLmgN/M/pev/+gwxIQA8sU7B4kTHvuiZEpSSbCnhFgpR/LQXWWfw7EBY2tB9AgMBAAECggEAGYHq+n/LDfK5SfBxEFNucT+eK7is9KWDfDLcEMZomk07XB6QFW4K7YZsdhxkVvnmxxTFaE03yewRu8BpZaz2tL/yy4R0RFV1aAzTuM57/YwwSb3zVkb9GSrJj04sEHu7B8SSwifiLScLMLdorygExx0mwwwRJb+XWAqa57R1jTpXnQjv4Nft6PVifMVnGymIjHHfVXUJkqXzmlbeWhdJhEwOfJRUMFwTp2Gi1v15Z5FHuIh5/NGxRsPje0s4YMSTbu4FDiMwlzFCy9zkDk/BZsLK9sYEcs+92Ay69z55ZjFWX7xXL7CyPLgTQ4y9bQE1TmqaAyVjyh64Bg/gKM6GYQKBgQDc5k8UDX/ld1i3452LtX8XaHpVY0yQGnbKzp4Zob1YQLUtVpuiTZHzhbM5pSVQFlCXkzhSiPyQvsmaArr6jFriWEUhbYgmIDlFa2StS0ZH0Gcw8rdf6q0vzujchZrRm546oQO/FgdWNz2K85olTVqoM2+4Hbpi8oFvFHSED1hFdQKBgQCV6W3w+Vo6IlUYBzkFKMoLGgjyPJreWotHP+FyzTTjqK0UE/i9P8PbjYpGTEHSOwfzg7DP/qR93arnAhIV6qM4RfrsDj+TuBMD9OaknNvRbl0EpZY5nvSNwqjp62CVPVLSQ9kBW7UtGGSuiVxJHxm9v6N/6PRjGeYNzejdLW0V6QKBgFo+pNPWaAfA6DfH/5cSAOf5QPEdbiv5A8r6+lASaZ5iYSIyncaC1jucxYmpVEMRur8R4BKn8DbaGtaWgvjU2lRaJ3PuoY6h34PiyfCaLg4sr9upbQz8fOBpMWzWEFfNsajWGwe34itwye24c6MFpSHOUbfwPTMrS4Gr46YH9tH5AoGAAS4pU3BjKXoDuYC1DjlX/eZik6WugnmsBw+VstWyyOgXFMVje/n4jM38fLk0+3bDhUNQLRMQMH2CTvdRNSL3zgWfCCTEk2ErpShUeI9Tm76GtPaozCNYQZV6xvy3cfVdpZIrLzuNnaFHiahDNcAs77WGkAdBhVY63Xj1kGg/J6ECgYBAdm6guGXpNCuTnryDN/w+Ufj3BKJm83d+qfBGAw7C3VhtTeARZzaUGugMSZTYC48Xx6K1dgwzYsIJtqkDZVuBLgRI39lXZbiKPGUSWhupzjNymW6Qkjw+SeULXPvwlAVGscm3K0zhf/ovgJiCXc1mzozO0yXWTZaqqHp0jnMZOQ==";

    public String getKeyId() {
        return keyId;
    }

    public TokenProperties setKeyId(String keyId) {
        this.keyId = keyId;
        return this;
    }

    public String getIss() {
        return iss;
    }

    public TokenProperties setIss(String iss) {
        this.iss = iss;
        return this;
    }

    public Set<String> getAud() {
        return aud;
    }

    public TokenProperties setAud(Set<String> aud) {
        this.aud = aud;
        return this;
    }

    public float getExp() {
        return exp;
    }

    public TokenProperties setExp(float exp) {
        this.exp = exp;
        return this;
    }

    public float getNbf() {
        return nbf;
    }

    public TokenProperties setNbf(float nbf) {
        this.nbf = nbf;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public TokenProperties setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public TokenProperties setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public TokenProperties setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
