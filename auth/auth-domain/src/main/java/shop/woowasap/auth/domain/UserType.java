package shop.woowasap.auth.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ROLE_USER(Constants.ROLE_USER, List.of(Constants.ROLE_USER)),
    ROLE_ADMIN(Constants.ROLE_ADMIN, List.of(Constants.ROLE_USER, Constants.ROLE_ADMIN)),
    ;

    private final String value;
    private final List<String> authorities;

    private static class Constants {

        private static final String ROLE_USER = "ROLE_USER";
        private static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
