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
        String userId = "testUserId";
        String password = "testPassword";
        SignUpRequest signUpRequest = new SignUpRequest(userId, password);

        // when
        ExtractableResponse<Response> response = signUp(signUpRequest);

        // then
        assertCreated(response);
    }

    @DisplayName("회원 가입 아이디 중복 시 409 Conflict 반환")
    @Test
    void givenDuplicatedUserIdWhenSignUpThenReturnConflict() {
        // given
        String userId = "testUserId";
        String password = "testPassword";
        SignUpRequest duplicatedSignUpRequest = new SignUpRequest(userId, password);

        signUp(duplicatedSignUpRequest);
        // when
        ExtractableResponse<Response> response = signUp(duplicatedSignUpRequest);

        // then
        assertConflict(response);
    }

    @DisplayName("회원 가입 아이디 잘못되었을시 400 Bad Request 반환")
    @Test
    void givenWrongUserIdWhenSignUpThenReturnBadRequest() {
        // given
        String password = "testPassword";
        SignUpRequest wrongUserIdSignUpRequest = new SignUpRequest(null, password);

        // when
        ExtractableResponse<Response> response = signUp(wrongUserIdSignUpRequest);

        // then
        assertBadRequest(response);
    }

    @DisplayName("회원 가입 비밀번호 잘못되었을 시 400 Bad Request 반환")
    @Test
    void givenWrongPasswordIdWhenSignUpThenReturnBadRequest() {
        // given
        String userId = "testUserId";
        SignUpRequest signUpRequest = new SignUpRequest(userId, null);

        // when
        ExtractableResponse<Response> response = signUp(signUpRequest);

        // then
        assertBadRequest(response);
    }
}
