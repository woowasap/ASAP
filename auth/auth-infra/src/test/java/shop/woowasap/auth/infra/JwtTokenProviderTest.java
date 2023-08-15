package shop.woowasap.auth.infra;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.auth.domain.in.response.UserResponse;

@SpringBootTest
@DisplayName("토큰 생성기 테스트")
@ContextConfiguration(classes = JwtTokenProvider.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockBean
    private Clock clock;

    @Value("${jwt.secret.key}")
    private String key;

    @Value("${jwt.secret.expiration}")
    private long expiration;

    @Nested
    @DisplayName("JwtTokenProvider.createToken 메소드")
    class JwtTokenProviderCreateTokenMethod {

        @Test
        @DisplayName("정상 입력 시 액세스 토큰에 클레임 정상 생성")
        void createTokenThenReturnJws() {
            // given
            final long id = 1L;
            final String username = "username";
            final String userType = "ROLE_USER";
            final UserResponse userResponse = new UserResponse(id, username, userType);
            final SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
            final Instant fixedInstant = Instant.parse("2023-08-15T00:00:01.00Z");
            when(Instant.now(clock)).thenReturn(fixedInstant);

            // when
            final String accessToken = tokenProvider.createToken(userResponse);
            final Claims claims = Jwts.parserBuilder()
                .setClock(() -> Date.from(fixedInstant))
                .setSigningKey(secretKey).build()
                .parseClaimsJws(accessToken)
                .getBody();

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(claims.get("userId").toString())
                    .isEqualTo(String.valueOf(id));
                softAssertions.assertThat(claims.get("userType")).isEqualTo(userType);
                softAssertions.assertThat(claims.getIssuedAt()).isEqualTo(Date.from(fixedInstant));
                softAssertions.assertThat(claims.getExpiration())
                    .isEqualTo(Date.from(fixedInstant.plusMillis(expiration)));
            });

        }
    }
}
