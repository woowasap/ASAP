package shop.woowasap.auth.infra.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.woowasap.auth.infra.AuthenticationProvider;

@RequiredArgsConstructor
public class JwtTokenAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader(AUTHORIZATION);
        Optional<Authentication> authentication = authenticationProvider
            .findAuthentication(bearerToken);
        authentication.ifPresent(
            auth -> SecurityContextHolder.getContext().setAuthentication(auth));

        filterChain.doFilter(request, response);
    }

}
