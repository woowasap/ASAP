package shop.woowasap.auth.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@SpringBootTest
@DisplayName("인증 생성기 테스트")
@ContextConfiguration(classes = AuthenticationProvider.class)
class AuthenticationProviderTest {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("AuthenticationProvider.findAuthentication 메소드는")
    class AuthenticationProviderFindAuthenticationMethod {

        @Test
        @DisplayName("정상 액세스 토큰 입력시 인증 반환")
        void findAuthenticationSuccess() {
            // given
            final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyVHlwZSI6IlJPTEVfVVNFUiIsInVzZXJJZCI6MSwiaWF0IjoxNjkyMDU3NjAxLCJleHAiOjE2OTIyNzM2MDF9.bVVPMLAxm2Vc6zy697wvJBSkYWPkAzEDP_LQ6ZJj9K8";
            final String bearerToken = "Bearer " + accessToken;
            when(jwtTokenProvider.validateToken(accessToken)).thenReturn(true);
            when(jwtTokenProvider.getUserId(accessToken)).thenReturn("1");
            when(jwtTokenProvider.getAuthorities(accessToken)).thenReturn(List.of("ROLE_USER"));

            // when
            Optional<Authentication> authentication = authenticationProvider
                .findAuthentication(bearerToken);

            // then
            assertThat(authentication).isNotEmpty();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"hello.world"})
        @DisplayName("잘못된 토큰 입력시 인증 반환")
        void findAuthenticationWrongToken(String bearerToken) {
            // when
            Optional<Authentication> authentication = authenticationProvider
                .findAuthentication(bearerToken);

            // then
            assertThat(authentication).isEmpty();
        }

        @Test
        @DisplayName("만료된 토큰 입력시 인증 반환")
        void findAuthenticationExpiredToken() {
            // given
            final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyVHlwZSI6IlJPTEVfVVNFUiIsInVzZXJJZCI6MSwiaWF0IjoxNjkyMDU3NjAxLCJleHAiOjE2OTIyNzM2MDF9.bVVPMLAxm2Vc6zy697wvJBSkYWPkAzEDP_LQ6ZJj9K8";
            final String bearerToken = "Bearer " + accessToken;
            when(jwtTokenProvider.validateToken(accessToken)).thenReturn(false);

            // when
            Optional<Authentication> authentication = authenticationProvider
                .findAuthentication(bearerToken);

            // then
            assertThat(authentication).isEmpty();
        }
    }

}
