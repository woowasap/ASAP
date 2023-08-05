package shop.woowasap.auth.domain;

import lombok.Builder;

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

    public void assertNotDuplicatedUserId(User user) {
        this.userId.assertNotDuplicated(user.userId);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }
}
