package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.AuthApiSupporter.login;
import static shop.woowasap.accept.support.api.AuthApiSupporter.signUp;
import static shop.woowasap.accept.support.valid.AuthValidator.assertLoginFail;
import static shop.woowasap.accept.support.valid.AuthValidator.assertLoginSuccess;
import static shop.woowasap.accept.support.valid.HttpValidator.assertBadRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertConflict;
import static shop.woowasap.accept.support.valid.HttpValidator.assertCreated;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.auth.controller.request.SignUpRequest;
import shop.woowasap.mock.dto.LoginRequest;

class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("회원 가입 정상 성공 시 201 CREATED 반환")
    void givenNormalWhenSignUpThenReturnCreated() {
        // given
        final String username = "testusername";
        final String password = "testPassword";
        final SignUpRequest signUpRequest = new SignUpRequest(username, password);

        // when
        final ExtractableResponse<Response> response = signUp(signUpRequest);

        // then
        assertCreated(response);
    }

    @Test
    @DisplayName("회원 가입 아이디 중복 시 409 Conflict 반환")
    void givenDuplicatedUsernameWhenSignUpThenReturnConflict() {
        // given
        final String username = "testusername";
        final String password = "testPassword";
        final SignUpRequest duplicatedSignUpRequest = new SignUpRequest(username, password);

        signUp(duplicatedSignUpRequest);
        // when
        final ExtractableResponse<Response> response = signUp(duplicatedSignUpRequest);

        // then
        assertConflict(response);
    }

    @Test
    @DisplayName("회원 가입 아이디 잘못되었을시 400 Bad Request 반환")
    void givenWrongUsernameWhenSignUpThenReturnBadRequest() {
        // given
        final String username = "USERIDWITHUPPER";
        final String password = "testPassword";
        final SignUpRequest wrongUsernameSignUpRequest = new SignUpRequest(username, password);

        // when
        final ExtractableResponse<Response> response = signUp(wrongUsernameSignUpRequest);

        // then
        assertBadRequest(response);
    }

    @Test
    @DisplayName("회원 가입 비밀번호 잘못되었을 시 400 Bad Request 반환")
    void givenWrongPasswordIdWhenSignUpThenReturnBadRequest() {
        // given
        final String username = "testusername";
        final SignUpRequest signUpRequest = new SignUpRequest(username, null);

        // when
        final ExtractableResponse<Response> response = signUp(signUpRequest);

        // then
        assertBadRequest(response);
    }

    @Test
    @DisplayName("로그인 정상 성공 시 200 OK 및 액세스 토큰 반환")
    void givenNormalWhenLoginThenReturnOkAndAccessToken() {
        // given
        final String username = "testusername";
        final String password = "testpassword";
        final SignUpRequest signUpRequest = new SignUpRequest(username, password);

        signUp(signUpRequest);

        final LoginRequest loginRequest = new LoginRequest(username, password);

        // when
        final ExtractableResponse<Response> response = login(loginRequest);

        // then
        assertLoginSuccess(response);
    }

    @Test
    @DisplayName("로그인 실패 시 400 Bad Request 반환")
    void givenWrongWhenLoginThenReturnBadRequest() {
        // given
        final String username = "testusername";
        final String password = "testpassword";
        final SignUpRequest signUpRequest = new SignUpRequest(username, password);

        signUp(signUpRequest);

        final String wrongPassword = "wrongPassword";
        final LoginRequest loginRequest = new LoginRequest(username, wrongPassword);

        // when
        final ExtractableResponse<Response> response = login(loginRequest);

        // then
        assertLoginFail(response);
    }
}
