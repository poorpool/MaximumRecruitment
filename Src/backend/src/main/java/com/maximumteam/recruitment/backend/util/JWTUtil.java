package com.maximumteam.recruitment.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JWTUtil {

    // 过期时间 7 天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    public static boolean verify(String token, String email, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("email", email)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static String getEmail(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("email").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 根据邮箱和密码签发 token
     * @param email
     * @param password
     * @return
     */
    public static String sign(String email, String password) {

        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(password);

        return JWT.create()
                .withClaim("email", email)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}