package shop.woowasap.auth.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(

    @NotNull(message = "유저 아이디는 비어있을 수 없습니다.")
    @NotBlank(message = "유저 아이디는 비어있을 수 없습니다.")
    String userId,

    @NotNull(message = "유저 비밀번호는 비어있을 수 없습니다.")
    @NotBlank(message = "유저 비밀번호는 비어있을 수 없습니다.")
    String password
) {

}
