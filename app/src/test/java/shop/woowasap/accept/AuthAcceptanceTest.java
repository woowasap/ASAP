package shop.woowasap.accept;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String API_VERSION = "/v1";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";

    @DisplayName("회원 가입 정상 성공 시 201 CREATED 반환")
    @Test
    void givenNormalWhenSignUpThenReturnCreated() {
        // given
        String userId = "testUserId";
        String password = "testPassword";
        Map<String, Object> params = new HashMap<>() {{
            put(USER_ID, userId);
            put(PASSWORD, password);
        }};

        // when
        ExtractableResponse<Response> response = signUp(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @DisplayName("회원 가입 아이디 중복 시 409 Conflict 반환")
    @Test
    void givenDuplicatedUserIdWhenSignUpThenReturnConflict() {
        // given
        String userId = "testUserId";
        String password = "testPassword";
        Map<String, Object> duplicatedParams = new HashMap<>() {{
            put(USER_ID, userId);
            put(PASSWORD, password);
        }};
        signUp(duplicatedParams);

        // when
        ExtractableResponse<Response> response = signUp(duplicatedParams);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CONFLICT);
    }

    @DisplayName("회원 가입 아이디 잘못되었을시 400 Bad Request 반환")
    @Test
    void givenWrongUserIdWhenSignUpThenReturnBadRequest() {
        // given
        String password = "testPassword";
        Map<String, Object> params = new HashMap<>() {{
            put(PASSWORD, password);
        }};

        // when
        ExtractableResponse<Response> response = signUp(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("회원 가입 비밀번호 잘못되었을 시 400 Bad Request 반환")
    @Test
    void givenWrongPasswordIdWhenSignUpThenReturnBadRequest() {
        // given
        String password = "testPassword";
        Map<String, Object> params = new HashMap<>() {{
            put(PASSWORD, password);
        }};

        // when
        ExtractableResponse<Response> response = signUp(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    private static ExtractableResponse<Response> signUp(Map<String, Object> params) {
        return RestAssured
            .given().log().all()
            .accept(JSON)
            .contentType(JSON)
            .body(params)
            .when().post(API_VERSION + "/signup")
            .then().log().all()
            .extract();
    }

}
