package com.socialmedia.socialmedia_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. get the Authorization header from the request
        String authHeader = request.getHeader("Authorization");

        // 2. if there is no token, skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. remove "Bearer " prefix to get just the token
        String token = authHeader.substring(7);

        // 4. check if token is valid
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. get email from token
        String email = jwtUtil.getEmailFromToken(token);

        // 6. if user is not already authenticated in this request
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 7. load user from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 8. create authentication object
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 9. tell Spring Security this user is authenticated
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 10. continue to the actual controller
        filterChain.doFilter(request, response);
    }
}