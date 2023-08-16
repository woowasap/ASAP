package shop.woowasap.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import shop.woowasap.auth.domain.in.request.UserLoginRequest;

public record LoginRequest(

    @NotNull
    @NotBlank
    String username,

    @NotNull
    @NotBlank
    String password

) {

    public UserLoginRequest toUserLoginRequest() {
        return new UserLoginRequest(username, password);
    }
}
