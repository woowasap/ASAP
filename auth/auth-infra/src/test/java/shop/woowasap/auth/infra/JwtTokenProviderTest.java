package shop.woowasap.auth.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.auth.domain.in.response.UserResponse;
import shop.woowasap.core.util.time.TimeUtil;

@SpringBootTest
@DisplayName("토큰 생성기 테스트")
@ContextConfiguration(classes = JwtTokenProvider.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @MockBean
    private TimeUtil timeUtil;

    @Value("${jwt.secret.key}")
    private String key;

    @Value("${jwt.secret.expiration}")
    private long expiration;

    @Nested
    @DisplayName("JwtTokenProvider.generateToken 메소드")
    class JwtTokenProviderGenerateTokenMethod {

        @Test
        @DisplayName("정상 입력 시 액세스 토큰에 클레임 정상 생성")
        void generateTokenThenReturnJws() {
            // given
            final long id = 1L;
            final String username = "username";
            final String userType = "ROLE_USER";
            final UserResponse userResponse = new UserResponse(id, username, userType);
            final SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
            final Instant fixedInstant = Instant.parse("2023-08-15T00:00:01.00Z");
            when(timeUtil.now()).thenReturn(fixedInstant);

            // when
            final String accessToken = tokenProvider.generateToken(userResponse);
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

    @Nested
    @DisplayName("JwtTokenProvider.validateToken 메소드")
    class JwtTokenProviderValidateTokenMethod {

        @Test
        @DisplayName("정상 입력 시 true 반환")
        void validateTokenThenReturnTrue() {
            // given
            final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyVHlwZSI6IlJPTEVfVVNFUiIsInVzZXJJZCI6MSwiaWF0IjoxNjkyMDU3NjAxLCJleHAiOjE2OTIyNzM2MDF9.bVVPMLAxm2Vc6zy697wvJBSkYWPkAzEDP_LQ6ZJj9K8";
            final Instant fixedInstant = Instant.parse("2023-08-15T00:00:02.00Z");
            when(timeUtil.now()).thenReturn(fixedInstant);

            // when
            final boolean result = tokenProvider.validateToken(accessToken);

            // then
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("비정상 입력 시 false 반환")
        @ValueSource(strings = {
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyVHlwZSI6IlJPTEVfVVNFUiIsInVzZXJJZCI6MSwiaWF0IjoxNjkyMDU3NjAxLCJleHAiOjE2OTIyNzM2MDF9.bVVPMLAxm2Vc6zy697wvJBSkYWPkAzEDP_LQ6ZJj9K8"
        })
        void validateTokenThenReturnFalse(final String accessToken) {
            // given
            final Instant fixedInstant = Instant.parse("2023-08-20T00:00:02.00Z");
            when(timeUtil.now()).thenReturn(fixedInstant);

            // when
            final boolean result = tokenProvider.validateToken(accessToken);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("JwtTokenProvider.extractAccessToken 메소드")
    class JwtTokenProviderExtractAccessTokenMethod {

        @Test
        @DisplayName("정상 입력 시 액세스 토큰에 클레임 정상 생성")
        void extractAccessTokenThenReturnAccessToken() {
            // given
            final String bearerToken = "Bearer e.a.e";

            // when
            final Optional<String> accessToken = tokenProvider.extractAccessToken(bearerToken);

            // then
            assertThat(accessToken).isNotEmpty();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = "asdasdasdasd")
        @DisplayName("정상 입력 시 액세스 토큰에 클레임 정상 생성")
        void extractAccessTokenWrongTokenThenReturnEmpty(final String bearerToken) {
            // when
            final Optional<String> accessToken = tokenProvider.extractAccessToken(bearerToken);

            // then
            assertThat(accessToken).isEmpty();
        }
    }


    @Nested
    @DisplayName("JwtTokenProvider.getAuthorities 메소드")
    class JwtTokenProviderGetAuthoritiesMethod {

        @Test
        @DisplayName("유저 토큰 입력시 리스트 반환")
        void getAuthoritiesUserThenReturnList() {
            // given
            final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyVHlwZSI6IlJPTEVfVVNFUiIsInVzZXJJZCI6MSwiaWF0IjoxNjkyMDU3NjAxLCJleHAiOjE2OTIyNzM2MDF9.bVVPMLAxm2Vc6zy697wvJBSkYWPkAzEDP_LQ6ZJj9K8";
            final Instant fixedInstant = Instant.parse("2023-08-15T00:00:02.00Z");
            when(timeUtil.now()).thenReturn(fixedInstant);

            // when
            final List<String> authorities = tokenProvider.getAuthorities(accessToken);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(authorities).hasSize(1);
                softAssertions.assertThat(authorities).containsExactly("ROLE_USER");
            });
        }

        @Test
        @DisplayName("어드민 토큰 입력시 리스트 반환")
        void getAuthoritiesAdminThenReturnList() {
            // given
            final String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJUeXBlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY5MjA1NzYwMSwiZXhwIjoxNjkyMjczNjAxfQ.l3lYH9ZaslvCSDffuxjWsmRYi0dKPQgFBnYLXJ4rhfo";
            final Instant fixedInstant = Instant.parse("2023-08-15T00:00:02.00Z");
            when(timeUtil.now()).thenReturn(fixedInstant);

            // when
            final List<String> authorities = tokenProvider.getAuthorities(accessToken);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(authorities).hasSize(2);
                softAssertions.assertThat(authorities).containsExactly("ROLE_USER", "ROLE_ADMIN");
            });
        }
    }

    @Nested
    @DisplayName("JwtTokenProvider.getAuthorities 메소드")
    class JwtTokenProviderGetUserIdMethod {

        @Test
        @DisplayName("유저 토큰 입력시 아이디 반환")
        void getUserIdThenReturnUserId() {
            // given
            final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyVHlwZSI6IlJPTEVfVVNFUiIsInVzZXJJZCI6MSwiaWF0IjoxNjkyMDU3NjAxLCJleHAiOjE2OTIyNzM2MDF9.bVVPMLAxm2Vc6zy697wvJBSkYWPkAzEDP_LQ6ZJj9K8";
            final Instant fixedInstant = Instant.parse("2023-08-15T00:00:02.00Z");
            when(timeUtil.now()).thenReturn(fixedInstant);

            // when
            final String userId = tokenProvider.getUserId(accessToken);

            // then
            assertThat(userId).isEqualTo("1");
        }
    }
}
