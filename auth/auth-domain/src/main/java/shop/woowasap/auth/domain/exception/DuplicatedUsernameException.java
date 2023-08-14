package shop.woowasap.auth.domain.exception;

public final class DuplicatedUsernameException extends AuthDomainBaseException {

    private static final String MESSAGE = "유저의 아이디가 중복되었습니다. 입력 값 : ";

    public DuplicatedUsernameException(String username) {
        super(MESSAGE + "\"" + username + "\"");
    }
}
