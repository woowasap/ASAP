package shop.woowasap.auth.domain.exception;

public final class DuplicatedUserIdException extends AuthDomainBaseException {

    private static final String MESSAGE = "유저의 아이디가 중복되었습니다. 입력 값 : ";

    public DuplicatedUserIdException(String userId) {
        super(MESSAGE + "\"" + userId + "\"");
    }
}
