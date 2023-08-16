package shop.woowasap.auth.infra;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.domain.in.TokenProvider;
import shop.woowasap.auth.domain.in.response.UserResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private static final String USER_ID = "userId";
    private static final String USER_TYPE = "userType";

    private final Clock clock;

    @Value("${jwt.secret.expiration}")
    private long expiration;

    @Value("${jwt.secret.key}")
    private String key;

    private SecretKey secretKey;
    private Map<String, Object> header;
    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.header = Map.of("alg", "HS256", "typ", "JWT");
        this.jwtParser = Jwts.parserBuilder()
            .setClock(() -> Date.from(Instant.now(clock)))
            .setSigningKey(secretKey)
            .build();
    }

    @Override
    public String generateToken(final UserResponse userResponse) {

        final Map<String, Object> claims = Map.of(
            USER_ID, userResponse.id(),
            USER_TYPE, userResponse.userType()
        );
        final Instant now = Instant.now(clock);

        final String accessToken = Jwts.builder()
            .setHeader(header)
            .setClaims(claims)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusMillis(expiration)))
            .signWith(secretKey)
            .compact();

        log.info("유저 {} 님이 로그인했습니다.", userResponse.username());
        return accessToken;
    }

    @Override
    public boolean validateToken(final String accessToken) {
        try {
            jwtParser.parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException: 만료된 JWT 토큰입니다.");
        } catch (JwtException e) {
            log.info("JwtException: 잘못된 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException: JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    @Override
    public List<String> getAuthorities(final String accessToken) {
        final String userType = jwtParser.parseClaimsJws(accessToken)
            .getBody()
            .get(USER_TYPE, String.class);
        return UserType.valueOf(userType).getAuthorities();
    }

    @Override
    public String getUserId(final String accessToken) {
        return String.valueOf(jwtParser.parseClaimsJws(accessToken)
            .getBody()
            .get(USER_ID, Long.class));
    }
}
