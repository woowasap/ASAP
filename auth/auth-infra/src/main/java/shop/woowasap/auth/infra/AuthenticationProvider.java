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
        final Optional<String> token = tokenProvider.extractAccessToken(bearerToken);
        if (token.isEmpty()) {
            return Optional.empty();
        }
        final String accessToken = token.get();
        if (!tokenProvider.validateToken(accessToken)) {
            return Optional.empty();
        }

        final Principal principal = getPrincipal(accessToken);
        final List<SimpleGrantedAuthority> grantedAuthorities = getGrantedAuthorities(accessToken);

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
            principal,
            accessToken,
            grantedAuthorities);
        return Optional.of(authentication);
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
