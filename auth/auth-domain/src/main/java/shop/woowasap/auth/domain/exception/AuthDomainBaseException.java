package shop.woowasap.auth.domain.exception;

public abstract class AuthDomainBaseException extends RuntimeException {

    protected AuthDomainBaseException(String message) {
        super(message);
    }
}
