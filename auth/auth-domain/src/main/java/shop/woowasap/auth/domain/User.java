package shop.woowasap.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class User {

    private final Long id;
    private final String userId;
    private final String password;

}
