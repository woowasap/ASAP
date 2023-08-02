package shop.woowasap.auth.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class User {

    private final Long id;
    private final String nickname;
    private final String username;
    private final String password;

}
