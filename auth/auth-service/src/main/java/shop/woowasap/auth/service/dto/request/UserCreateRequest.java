package shop.woowasap.auth.service.dto.request;

import lombok.Builder;

@Builder
public record UserCreateRequest(

    String userId,
    String password

) {

}
