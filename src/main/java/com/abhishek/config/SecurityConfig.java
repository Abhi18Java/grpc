package com.abhishek.config;

import com.abhishek.EmployeeServiceGrpc;
import com.abhishek.jwt.JwtAuthProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Value("${jwt.signing.key}")
    private String jwtKey;

    private final JwtAuthProvider jwtAuthProvider;

    public SecurityConfig(JwtAuthProvider jwtAuthProvider) {
        this.jwtAuthProvider = jwtAuthProvider;
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthProvider);
    }

    @Bean
    GrpcAuthenticationReader grpcAuthenticationReader() {  //when the user sends a request this bean will validate if that request is authenticated or not
        return new BearerAuthenticationReader(token -> {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtKey)
                    .parseClaimsJws(token)
                    .getBody();
            List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            User user = new User(claims.getSubject(), "" , authorities);
            return new UsernamePasswordAuthenticationToken(user, token, authorities);
        });
    }

    @Bean
    GrpcSecurityMetadataSource grpcSecurityMetadataSource() {
        ManualGrpcSecurityMetadataSource manualGrpcSecurityMetadataSource = new ManualGrpcSecurityMetadataSource();
        manualGrpcSecurityMetadataSource.setDefault(AccessPredicate.permitAll());
        manualGrpcSecurityMetadataSource.set(EmployeeServiceGrpc.getGetEmployeeInfoMethod(), AccessPredicate.hasRole("ADMIN"));
        return manualGrpcSecurityMetadataSource;
    }

    @Bean
    AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(new AccessPredicateVoter());
        return new UnanimousBased(accessDecisionVoters);
    }

}
