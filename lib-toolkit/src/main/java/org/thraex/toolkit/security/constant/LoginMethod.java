package org.thraex.toolkit.security.constant;

/**
 * @author 鬼王
 * @date 2022/05/13 15:37
 */
public enum LoginMethod {

    /**
     * Username and password login
     */
    USERNAME_PASSWORD,

    /**
     * SMS or IM verification code login
     */
    VERIFICATION_CODE,

    /**
     * {@link LoginMethod#USERNAME_PASSWORD} + {@link LoginMethod#VERIFICATION_CODE}
     */
    TWO_FACTOR;

}
