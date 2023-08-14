package shop.woowasap.auth.domain.in.request;

public record UserLoginRequest(

    String username,
    String password
) {

}
