package shop.woowasap.accept.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import shop.woowasap.shop.domain.api.cart.response.CartResponse;

public final class CartValidator {

    private CartValidator() {
    }

    public static void assertCartProductsFound(ExtractableResponse<Response> result,
        CartResponse expected) {
        HttpValidator.assertOk(result);

        CartResponse cartResponse = result.as(CartResponse.class);

        assertThat(cartResponse).usingRecursiveComparison().ignoringFields("cartId")
            .isEqualTo(expected);
    }

}
