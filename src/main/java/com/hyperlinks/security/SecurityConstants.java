package com.hyperlinks.security;

public interface SecurityConstants {

    String SECRET = "EpicSecret";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    String SIGN_UP_URL = "/api/signup";
    String LOGIN_URL = "/api/login";
    int EXPIRATION_DATE = 864000000;
}
