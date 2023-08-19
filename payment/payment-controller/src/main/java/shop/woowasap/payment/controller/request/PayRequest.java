package shop.woowasap.payment.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PayRequest(

    @NotNull
    @NotBlank
    String payType,

    @NotNull
    Boolean isSuccess

) {

}
