package shop.woowasap.auth.infra;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
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

    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.header = Map.of("alg", "HS256", "typ", "JWT");
    }

    @Override
    public String createToken(final UserResponse userResponse) {

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
    public UserType parseToken(String accessToken) {
        return null;
    }
}
