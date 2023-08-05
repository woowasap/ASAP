package shop.woowasap.auth.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import shop.woowasap.auth.domain.exception.PasswordValidateException;

@DisplayName("유저 비밀번호 테스트")
class PasswordTest {

    @Test
    @DisplayName("유저 비밀번호 정상생성 테스트")
    void passwordCreateSuccessThenReturn() {
        // given
        String digest = "{bcrypt}$2aasdlkjlaksjdlasjd";

        // when
        Password password = assertDoesNotThrow(() -> Password.of(digest));

        // then
        assertEquals(digest, password.getDigest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("유저 비밀번호 비어있으면 생성시 예외 발생")
    void nullOrBlankDigestThenThrow(String digest) {
        // when
        PasswordValidateException exception = assertThrows(PasswordValidateException.class,
            () -> Password.of(digest));

        // then
        assertEquals("비밀번호는 비어있을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유저 비밀번호가 너무 길면 생성시 예외 발생")
    void tooLongDigestThenThrow() {
        // given
        String digest = "a".repeat(256);

        // when
        PasswordValidateException exception = assertThrows(PasswordValidateException.class,
            () -> Password.of(digest));

        // then
        assertEquals("비밀번호는 255자 이하여야 합니다.", exception.getMessage());
    }
}
