package shop.woowasap.auth.domain.in.request;

import lombok.Builder;

@Builder
public record UserCreateRequest(

    String userId,
    String password

) {

}
