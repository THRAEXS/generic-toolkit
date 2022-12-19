package org.thraex.toolkit.security.authentication;

/**
 * @author 鬼王
 * @date 2022/05/13 15:37
 */
public enum AuthenticationMethod {

    /**
     * Username and password authentication
     */
    USERNAME_PASSWORD,

    /**
     * Verification code authentication. e.g.: SMS / IM
     */
    VERIFICATION_CODE,

    /**
     * {@link AuthenticationMethod#USERNAME_PASSWORD} + {@link AuthenticationMethod#VERIFICATION_CODE}
     */
    TWO_FACTOR

}
