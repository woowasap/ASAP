package shop.woowasap.auth.domain.in.request;

public record UserCreateRequest(

    String username,
    String password

) {

}
