package io.reactivestax.active_life_canada.constant;

public class SecurityConstants {
    public static final String SECRET = "TestSecrEtKeyF0rJwtHash1ng";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String USER_ID = "userId";
}
