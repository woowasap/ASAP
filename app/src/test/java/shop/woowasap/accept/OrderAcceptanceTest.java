package shop.woowasap.accept;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.OrderApiSupporter;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.OrderValidator;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;

@DisplayName("Order 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 바로 구매 API는 productId와 quantity를 받아 상품을 구매한후, Created와 Location을 응답한다.")
    void returnCreatedAndLocationWhenSuccessToBuyProduct() {
        // given
        final String token = "TOKEN";

        final long productId = 1L;
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(productId, orderProductRequest,
                token);

        // then
        OrderValidator.assertOrdered(result);
    }

    @Test
    @DisplayName("상품 바로 구매 API는 productId에 해당하는 product를 찾을 수 없는경우, 400 BadRequest를 응답한다.")
    void returnBadRequestWhenCannotFindMatchedProduct() {
        // given
        final String token = "TOKEN";

        final long notExistProductId = 123L;
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(notExistProductId,
                orderProductRequest, token);

        // then
        HttpValidator.assertBadRequest(result);
    }
}
