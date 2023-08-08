package shop.woowasap.auth.service.dto.request;

import lombok.Builder;
import shop.woowasap.auth.domain.User;

@Builder
public record UserCreateRequest(

    String userId,
    String password

) {

    public User toDomain() {
        return User.builder().userId(userId).password(password).build();
    }
}
