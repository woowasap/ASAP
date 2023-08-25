package shop.woowasap.auth.domain.in.response;

public record UserResponse(
    Long id,
    String username,
    String userType
) {

}
