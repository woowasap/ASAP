package shop.woowasap.auth.domain.exception;

public class LoginFailException extends AuthDomainBaseException {

    private static final String MESSAGE = "아이디와 비밀번호를 잘못 입력하셨습니다. 아이디와 비밀번호를 확인해주세요.";

    public LoginFailException() {
        super(MESSAGE);
    }
}
