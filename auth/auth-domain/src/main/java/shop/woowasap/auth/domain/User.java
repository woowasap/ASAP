package shop.woowasap.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class User {

    private final Long id;
    private final UserId userId;
    private final Password password;

    @Builder
    public User(Long id, String userId, String password) {
        this.id = id;
        this.userId = UserId.of(userId);
        this.password = Password.of(password);
    }
}
