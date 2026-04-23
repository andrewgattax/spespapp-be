package it.trinex.spespappbe.security;

import it.trinex.spespappbe.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Filtro JWT che estrae il token dall'header Authorization e imposta il contesto di sicurezza.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;

        Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> "jwt".equals(c.getName()))
                .findFirst();
        if (jwtCookie.isPresent()) {
            jwt = jwtCookie.get().getValue();
        } else {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
            }
        }

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = null;
        try {
            username = jwtService.estraiUsername(jwt);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("X-Flag-Unauthorized", "true");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error("UNAUTHORIZED")
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .path(request.getRequestURI())
                    .message("Invalid JWT token")
                    .build();

            new ObjectMapper().writeValue(response.getWriter(), errorResponse);

            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {



            // Estrai dettagli dal token senza accedere al DB
            final String ruolo = jwtService.estraiRuolo(jwt);
            final Long userId = jwtService.estraiUserId(jwt);
            final Integer storiePubblicate = jwtService.estraiClaimExtra(jwt, "storiePubblicate");
            final Integer templateSalvati = jwtService.estraiClaimExtra(jwt, "templateSalvati");
            final Integer storieCondivise = jwtService.estraiClaimExtra(jwt, "storieCondivise");
            List<SimpleGrantedAuthority> authorities = ruolo != null
                    ? List.of(new SimpleGrantedAuthority("ROLE_" + ruolo))
                    : List.of();

            UserDetails principal = new JwtUserPrincipal(userId, username, storiePubblicate, templateSalvati, storieCondivise, authorities);

            if (jwtService.isTokenValido(jwt, principal)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("X-Flag-Unauthorized", "true");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error("UNAUTHORIZED")
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .path(request.getRequestURI())
                        .message("Invalid JWT token")
                        .build();

                new ObjectMapper().writeValue(response.getWriter(), errorResponse);

                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
