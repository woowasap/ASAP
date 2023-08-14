package shop.woowasap.auth.domain;

import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import shop.woowasap.auth.domain.exception.DuplicatedUsernameException;
import shop.woowasap.auth.domain.exception.UsernameValidateException;

@Getter
@EqualsAndHashCode
final class UserName {

    private static final int USER_NAME_MIN_LENGTH = 5;
    private static final int USER_NAME_MAX_LENGTH = 25;
    private static final String USER_NAME_REGEX =
        "^[a-z0-9]{" + USER_NAME_MIN_LENGTH + "," + USER_NAME_MAX_LENGTH + "}$";
    private static final Pattern USER_NAME_REGEX_PATTERN = Pattern.compile(USER_NAME_REGEX);

    private final String value;

    private UserName(final String value) {
        validate(value);
        this.value = value;
    }

    public static UserName of(final String value) {
        return new UserName(value);
    }

    private void validate(final String value) {
        if (value == null) {
            throw new UsernameValidateException("유저의 아이디는 있어야 합니다.");
        }
        if (!USER_NAME_REGEX_PATTERN.matcher(value).matches()) {
            throw new UsernameValidateException(
                "유저의 아이디는 " + USER_NAME_MIN_LENGTH + "이상, " + USER_NAME_MAX_LENGTH
                    + "자 이하의 소문자와 숫자만 가능합니다.");
        }
    }

    public void assertNotDuplicated(final UserName userName) {
        if (this.value.equals(userName.value)) {
            throw new DuplicatedUsernameException(userName.value);
        }
    }
}
