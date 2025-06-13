package com.chatapp.ChatApp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenValidator extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        if(jwt != null) {
            try {
                String username = tokenProvider.getEmailFromToken(jwt);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                if (StringUtils.hasText(username) && tokenProvider.isTokenValid(jwt, userDetails)) {
                    log.info("VALID JWT FOR {}", username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }else {
                    log.error("JWT EXPIRED OR INVALID FOR {}", username);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                    return;
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
                throw new BadCredentialsException("invalid token recieved...");
            }
        }
        filterChain.doFilter(request, response);
    }
}
