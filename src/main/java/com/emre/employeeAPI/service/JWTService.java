package com.emre.employeeAPI.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.emre.employeeAPI.configuration.IApplicationConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JWTService {

    private IApplicationConfig applicationConfig;

    private String secret;

    @Getter
    private String issuer;

    private Algorithm algorithmHS;
    private JWTVerifier verifier;

    public JWTService(IApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @PostConstruct
    public synchronized void postConstruct() {
        this.secret = this.applicationConfig.getJwtSecret();
        this.issuer = this.applicationConfig.getJwtIssuer();
        this.algorithmHS = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithmHS).withIssuer(issuer).build();
    }

    public boolean verifyToken(String token) {
        verifier.verify(token);
        return true;
    }

    public String getEmailFromHeader(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);

            return jwt.getSubject();
        } catch (Exception ex) {
            log.error("JWT token is not verified: " + token, ex);
            return null;
        }
    }

    public String createToken(String email) {

        JWTCreator.Builder builder = JWT.create();

        builder.withSubject(email);

        Instant now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        Date expires = Date.from(now.plusSeconds(applicationConfig.getAuthTokenExpireSeconds()));
        Date issuedAt = Date.from(now);
        return builder.withIssuer(issuer).withIssuedAt(issuedAt).withExpiresAt(expires).sign(algorithmHS);
    }

}