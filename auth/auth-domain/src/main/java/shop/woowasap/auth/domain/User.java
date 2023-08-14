package shop.woowasap.auth.domain;

import lombok.Builder;

public final class User {

    private final Long id;
    private final UserName username;
    private final Password password;

    @Builder
    public User(final Long id, final String username, final String password) {
        this.id = id;
        this.username = UserName.of(username);
        this.password = Password.of(password);
    }

    public void assertNotDuplicatedUsername(final User user) {
        this.username.assertNotDuplicated(user.username);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }
}
