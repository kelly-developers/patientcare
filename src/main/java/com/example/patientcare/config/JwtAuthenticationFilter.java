package com.example.patientcare.config;

import com.example.patientcare.security.CustomUserDetailsService;
import com.example.patientcare.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            // Skip JWT processing for auth endpoints
            if (isAuthEndpoint(request)) {
                logger.debug("Skipping JWT filter for auth endpoint: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            if (jwt != null) {
                if (jwtService.validateJwtToken(jwt)) {
                    String username = jwtService.getUsernameFromJwtToken(jwt);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authenticated user: {}", username);
                } else {
                    logger.warn("Invalid JWT token for request: {}", request.getRequestURI());
                    // Don't set authentication - let it remain anonymous
                }
            } else {
                logger.debug("No JWT token found in request for: {}", request.getRequestURI());
            }
        } catch (UsernameNotFoundException e) {
            logger.error("User not found for JWT token: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String token = headerAuth.substring(7);
            if (StringUtils.hasText(token)) {
                logger.debug("JWT token found in request");
                return token;
            }
        }

        logger.debug("No JWT token found in request");
        return null;
    }

    private boolean isAuthEndpoint(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api/auth/") &&
                !requestURI.equals("/api/auth/verify") &&
                !requestURI.equals("/api/auth/refresh");
    }
}