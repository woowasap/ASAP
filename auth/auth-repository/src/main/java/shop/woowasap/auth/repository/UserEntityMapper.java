package shop.woowasap.auth.repository;

import shop.woowasap.auth.domain.User;
import shop.woowasap.auth.domain.UserType;
import shop.woowasap.auth.repository.entity.UserEntity;
import shop.woowasap.auth.repository.entity.UserEntityType;

public class UserEntityMapper {

    private UserEntityMapper() {
    }

    public static User toDomain(final UserEntity userEntity) {
        return User.builder()
            .id(userEntity.getId())
            .username(userEntity.getUsername())
            .password(userEntity.getPassword())
            .userType(UserType.valueOf(userEntity.getUserType().getValue()))
            .build();
    }

    public static UserEntity toEntity(final User user) {
        return UserEntity.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .userType(UserEntityType.valueOf(user.getUserType().getValue()))
            .build();
    }
}
