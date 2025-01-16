package com.abhishek.service;

import com.abhishek.AuthServiceGrpc;
import com.abhishek.JwtRequest;
import com.abhishek.JwtToken;
import com.abhishek.jwt.JwtAuthProvider;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    @Value("${jwt.signing.key}")
    private String jwtKey;

    private final JwtAuthProvider jwtAuthProvider;

    public AuthGrpcService(JwtAuthProvider jwtAuthProvider) {
        this.jwtAuthProvider = jwtAuthProvider;
    }

    @Override
    public void authorize(JwtRequest request, StreamObserver<JwtToken> responseObserver) {

        Authentication authentication = jwtAuthProvider.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

        Instant now = Instant.now();
        Instant expire = now.plus(1, ChronoUnit.HOURS);

        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        responseObserver.onNext(JwtToken.newBuilder().setJwtToken(Jwts.builder()
                .setSubject((String) authentication.getPrincipal())
                .claim("auth", authorities)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expire))
                .signWith(SignatureAlgorithm.HS512, jwtKey)
                .compact()).build());

        responseObserver.onCompleted();

    }
}
