package shop.woowasap.auth.domain;

import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import shop.woowasap.auth.domain.exception.UserIdValidateException;

@Getter
@EqualsAndHashCode
public final class UserId {

    private static final int USER_ID_MIN_LENGTH = 5;
    private static final int USER_ID_MAX_LENGTH = 25;
    private static final String USER_ID_REGEX =
        "^[a-z0-9]{" + USER_ID_MIN_LENGTH + "," + USER_ID_MAX_LENGTH + "}$";
    private static final Pattern USER_ID_REGEX_PATTERN = Pattern.compile(USER_ID_REGEX);

    private final String value;

    private UserId(final String value) {
        validate(value);
        this.value = value;
    }

    public static UserId of(final String value) {
        return new UserId(value);
    }

    private void validate(final String value) {
        if (value == null) {
            throw new UserIdValidateException("유저의 아이디는 있어야 합니다.");
        }
        if (!USER_ID_REGEX_PATTERN.matcher(value).matches()) {
            throw new UserIdValidateException(
                "유저의 아이디는 " + USER_ID_MIN_LENGTH + "이상, " + USER_ID_MAX_LENGTH
                    + "자 이하의 소문자와 숫자만 가능합니다.");
        }
    }
}
