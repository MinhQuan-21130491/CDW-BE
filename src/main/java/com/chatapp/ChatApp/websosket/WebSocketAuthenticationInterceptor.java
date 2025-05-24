package com.chatapp.ChatApp.websosket;

import com.chatapp.ChatApp.config.CustomUserDetailService;
import com.chatapp.ChatApp.config.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class WebSocketAuthenticationInterceptor  implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;

    public WebSocketAuthenticationInterceptor(TokenProvider tokenProvider, CustomUserDetailService userDetailService) {
        this.tokenProvider = tokenProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        String query = uri.getQuery(); // token=xxx

        if (query != null && query.startsWith("token=")) {
            String token = query.replace("token=", "");

            String username = tokenProvider.getEmailFromToken(token);
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            if (tokenProvider.isTokenValid(token, userDetails)) {
                attributes.put("email", username);
                return true;
            }
        }

        response.setStatusCode(HttpStatusCode.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
