package shop.woowasap.auth.domain.exception;

public abstract class AuthDomainBaseException extends RuntimeException {

    public AuthDomainBaseException(String message) {
        super(message);
    }
}
