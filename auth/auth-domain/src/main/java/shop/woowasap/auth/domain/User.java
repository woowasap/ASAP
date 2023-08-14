package shop.woowasap.auth.domain;

import lombok.Builder;

public final class User {

    private final Long id;
    private final UserName username;
    private final Password password;
    private final UserType userType;

    @Builder
    public User(final Long id, final String username, final String password,
        final UserType userType) {
        this.id = id;
        this.username = UserName.of(username);
        this.password = Password.of(password);
        this.userType = userType;
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

    public UserType getUserType() {
        return userType;
    }
}
