package shop.woowasap.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.Payment;
import shop.woowasap.order.service.support.fixture.ProductFixture;
import shop.woowasap.shop.app.api.ProductConnector;

@DisplayName("OrderService 클래스")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderService.class)
class OrderServiceTest {

    @Autowired
    private OrderUseCase orderUseCase;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private ProductConnector productConnector;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Payment payment;

    @Nested
    @DisplayName("order 메소드는")
    class orderMethod {

        @Test
        @DisplayName("userId, productId, OrderProductRequest 를 받아 주문을 한다.")
        void successOrderWhenReceiveUserIdAndOrderProductRequest() {
            // given
            final long userId = 1L;
            final long productId = 2L;
            final int quantity = 3;
            final OrderProductRequest orderProductRequest = new OrderProductRequest(userId, productId, quantity);

            final long orderId = 1L;
            when(idGenerator.generate()).thenReturn(orderId);
            when(productConnector.getById(productId)).thenReturn(ProductFixture.getDefaultBuilder()
                    .build());
            when(payment.pay(userId)).thenReturn(true);

            // when
            final long result = orderUseCase.orderProduct(orderProductRequest);

            // then
            assertThat(result).isEqualTo(orderId);
        }

        @Test
        @DisplayName("주문을 실패하면, DoesNotOrderedException을 던진다.")
        void throwDoesNotOrderedExceptionWhenFailToOrder() {
            // given
            final long userId = 1L;
            final long productId = 2L;
            final int quantity = 3;
            final OrderProductRequest orderProductRequest = new OrderProductRequest(userId, productId, quantity);

            final long orderId = 1L;
            when(idGenerator.generate()).thenReturn(orderId);
            when(productConnector.getById(productId)).thenReturn(ProductFixture.getDefaultBuilder()
                    .build());
            when(payment.pay(userId)).thenReturn(false);

            // when
            final Exception exception = catchException(() -> orderUseCase.orderProduct(orderProductRequest));

            // then
            assertThat(exception).isInstanceOf(DoesNotOrderedException.class);
        }
    }
}
