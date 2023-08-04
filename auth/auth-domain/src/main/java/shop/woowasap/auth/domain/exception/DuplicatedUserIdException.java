package shop.woowasap.auth.domain.exception;

public final class DuplicatedUserIdException extends AuthDomainBaseException {

    public DuplicatedUserIdException(String message) {
        super(message);
    }
}
