package shop.woowasap.auth.infra;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import shop.woowasap.auth.domain.in.TokenProvider;

@Service
@RequiredArgsConstructor
public class AuthenticationProvider {

    private final TokenProvider tokenProvider;

    public Optional<Authentication> findAuthentication(final String bearerToken) {
        return tokenProvider.extractAccessToken(bearerToken)
            .filter(tokenProvider::validateToken)
            .map(this::createAuthentication);
    }

    private Authentication createAuthentication(final String accessToken) {
        final Principal principal = getPrincipal(accessToken);
        final List<SimpleGrantedAuthority> grantedAuthorities = getGrantedAuthorities(accessToken);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, grantedAuthorities);
    }

    private Principal getPrincipal(final String accessToken) {
        final String userId = tokenProvider.getUserId(accessToken);
        return () -> userId;
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(final String accessToken) {
        final List<String> authorities = tokenProvider.getAuthorities(accessToken);
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
