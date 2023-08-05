package shop.woowasap.auth.domain;

import lombok.Getter;
import shop.woowasap.auth.domain.exception.PasswordValidateException;

@Getter
final class Password {

    private static final int PASSWORD_MAX_LENGTH = 255;
    private final String value;

    private Password(String value) {
        validate(value);
        this.value = value;
    }

    public static Password of(String digest) {
        return new Password(digest);
    }

    private void validate(String digest) {
        if (digest == null || digest.isBlank()) {
            throw new PasswordValidateException("비밀번호는 비어있을 수 없습니다.");
        }
        if (digest.length() > PASSWORD_MAX_LENGTH) {
            throw new PasswordValidateException("비밀번호는 255자 이하여야 합니다.");
        }
    }
}
