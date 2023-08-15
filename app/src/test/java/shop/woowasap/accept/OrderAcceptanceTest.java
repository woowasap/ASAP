package shop.woowasap.accept;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.OrderApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.OrderValidator;
import shop.woowasap.mock.dto.OrderProductResponse;
import shop.woowasap.mock.dto.OrderResponse;
import shop.woowasap.mock.dto.OrdersResponse;
import shop.woowasap.mock.dto.ProductsResponse;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;

@DisplayName("Order 인수테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품 바로 구매 API는 productId와 quantity를 받아 상품을 구매한후, Created와 Location을 응답한다.")
    void returnCreatedAndLocationWhenSuccessToBuyProduct() {
        // given
        final String token = "TOKEN";

        final long productId = getRandomProduct().productId();
        final int quantity = 2;
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(productId,
            orderProductRequest,
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
        final OrderProductQuantityRequest orderProductRequest = new OrderProductQuantityRequest(
            quantity);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.orderProduct(
            notExistProductId,
            orderProductRequest, token);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("구매내역 전체 조회 API는 사용자의 구매내역을 응답한다")
    void returnOrders() {
        // given
        final String token = "TOKEN";
        final ProductsResponse.Product product = getRandomProduct();
        final long quantity = 1;
        final OrderProductQuantityRequest orderProductQuantityRequest = new OrderProductQuantityRequest(
            quantity);

        OrderApiSupporter.orderProduct(product.productId(), orderProductQuantityRequest, token);

        final OrderProductResponse expectedOrderProductResponse = new OrderProductResponse(
            product.productId(), product.name());
        final OrderResponse expectedOrderResponse = new OrderResponse(1L,
            List.of(expectedOrderProductResponse), product.price(), quantity, LocalDateTime.now());
        final OrdersResponse expected = new OrdersResponse(List.of(expectedOrderResponse), 1, 1);

        // when
        final ExtractableResponse<Response> result = OrderApiSupporter.getAllProducts(token);

        // then
        OrderValidator.assertOrders(result, expected);
    }

    private ProductsResponse.Product getRandomProduct() {
        return ShopApiSupporter.getAllProducts().as(ProductsResponse.class)
            .products().get(0);
    }
}
