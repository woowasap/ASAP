package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.AuthApiSupporter.signUp;
import static shop.woowasap.accept.support.valid.HttpValidator.assertBadRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertConflict;
import static shop.woowasap.accept.support.valid.HttpValidator.assertCreated;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.mock.dto.SignUpRequest;

class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 가입 정상 성공 시 201 CREATED 반환")
    @Test
    void givenNormalWhenSignUpThenReturnCreated() {
        // given
        final String userId = "testuserid";
        final String password = "testPassword";
        final SignUpRequest signUpRequest = new SignUpRequest(userId, password);

        // when
        final ExtractableResponse<Response> response = signUp(signUpRequest);

        // then
        assertCreated(response);
    }

    @DisplayName("회원 가입 아이디 중복 시 409 Conflict 반환")
    @Test
    void givenDuplicatedUserIdWhenSignUpThenReturnConflict() {
        // given
        final String userId = "testuserid";
        final String password = "testPassword";
        final SignUpRequest duplicatedSignUpRequest = new SignUpRequest(userId, password);

        signUp(duplicatedSignUpRequest);
        // when
        final ExtractableResponse<Response> response = signUp(duplicatedSignUpRequest);

        // then
        assertConflict(response);
    }

    @DisplayName("회원 가입 아이디 잘못되었을시 400 Bad Request 반환")
    @Test
    void givenWrongUserIdWhenSignUpThenReturnBadRequest() {
        // given
        final String userId = "USERIDWITHUPPER";
        final String password = "testPassword";
        final SignUpRequest wrongUserIdSignUpRequest = new SignUpRequest(userId, password);

        // when
        final ExtractableResponse<Response> response = signUp(wrongUserIdSignUpRequest);

        // then
        assertBadRequest(response);
    }

    @DisplayName("회원 가입 비밀번호 잘못되었을 시 400 Bad Request 반환")
    @Test
    void givenWrongPasswordIdWhenSignUpThenReturnBadRequest() {
        // given
        final String userId = "testuserid";
        final SignUpRequest signUpRequest = new SignUpRequest(userId, null);

        // when
        final ExtractableResponse<Response> response = signUp(signUpRequest);

        // then
        assertBadRequest(response);
    }
}
