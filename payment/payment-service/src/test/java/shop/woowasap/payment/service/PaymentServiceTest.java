package shop.woowasap.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.core.util.time.TimeUtil;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.in.OrderConnector;
import shop.woowasap.order.domain.in.event.PaySuccessEvent;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.exception.DoesNotFindOrderException;
import shop.woowasap.payment.domain.exception.PayUserNotMatchException;
import shop.woowasap.payment.domain.in.request.PaymentRequest;
import shop.woowasap.payment.domain.in.response.PaymentResponse;
import shop.woowasap.payment.domain.out.PaymentRepository;

@RecordApplicationEvents
@ExtendWith(SpringExtension.class)
@DisplayName("PaymentService 클래스")
@ContextConfiguration(classes = PaymentService.class)
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ApplicationEvents applicationEvents;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private OrderConnector orderConnector;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private TimeUtil timeUtil;

    private Instant instant = Instant.parse("2023-08-15T00:00:02.00Z");

    @Nested
    @DisplayName("pay 메소드")
    class PayMethod {

        @Test
        @DisplayName("isSuccess true면 true 반환")
        void payIsSuccessTrueThenReturnTrue() {
            // given
            final long orderId = 123L;
            final long userId = 12345L;
            final PayType payType = PayType.CARD;
            final boolean isSuccess = true;
            final Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .orderProducts(List.of(
                    OrderProduct.builder()
                        .productId(1L)
                        .name("name")
                        .quantity(1L)
                        .price("10000")
                        .startTime(Instant.MIN)
                        .endTime(Instant.MAX)
                        .build()))
                .createdAt(instant)
                .build();

            final PaymentRequest paymentRequest = new PaymentRequest(orderId, userId, payType,
                isSuccess);

            when(orderConnector.findByOrderId(orderId)).thenReturn(Optional.of(order));
            when(idGenerator.generate()).thenReturn(1L);
            when(timeUtil.now()).thenReturn(instant.plusMillis(100L));
            when(paymentRepository.save(any())).thenReturn(null);

            // when
            final PaymentResponse result = paymentService.pay(paymentRequest);

            // then
            long count = applicationEvents.stream(PaySuccessEvent.class).count();
            assertThat(result.isSuccess()).isTrue();
            assertThat(count).isOne();
        }

        @Test
        @DisplayName("isSuccess false면 false 반환")
        void payIsSuccessFalseThenReturnFalse() {
            // given
            final long orderId = 123L;
            final long userId = 12345L;
            final PayType payType = PayType.CARD;
            final boolean isSuccess = false;
            final Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .orderProducts(List.of(
                    OrderProduct.builder()
                        .productId(1L)
                        .name("name")
                        .quantity(1L)
                        .price("10000")
                        .startTime(Instant.MIN)
                        .endTime(Instant.MAX)
                        .build()))
                .createdAt(instant)
                .build();

            final PaymentRequest paymentRequest = new PaymentRequest(orderId, userId, payType,
                isSuccess);

            when(orderConnector.findByOrderId(orderId)).thenReturn(Optional.of(order));
            when(idGenerator.generate()).thenReturn(1L);
            when(timeUtil.now()).thenReturn(instant.plusMillis(100L));
            when(paymentRepository.save(any())).thenReturn(null);

            // when
            final PaymentResponse result = paymentService.pay(paymentRequest);

            // then
            assertThat(result.isSuccess()).isFalse();
        }

        @Test
        @DisplayName("orderId 없으면 예외 발생")
        void payOrderIdNotFournThenThrow() {
            // given
            final long orderId = 123L;
            final long userId = 12345L;
            final PayType payType = PayType.CARD;
            final boolean isSuccess = true;

            final PaymentRequest paymentRequest = new PaymentRequest(orderId, userId, payType,
                isSuccess);

            when(orderConnector.findByOrderId(orderId)).thenReturn(Optional.empty());

            // when
            final Exception exception = catchException(() -> paymentService.pay(paymentRequest));

            // then
            assertThat(exception).isInstanceOf(DoesNotFindOrderException.class);
        }

        @Test
        @DisplayName("userId 다르면 예외 발생")
        void payUserIdNotMatchThenThrow() {
            // given
            final long orderId = 123L;
            final long userId = 12345L;
            final PayType payType = PayType.CARD;
            final boolean isSuccess = false;
            final Order order = Order.builder()
                .id(orderId)
                .userId(99999L)
                .orderProducts(List.of(
                    OrderProduct.builder()
                        .productId(1L)
                        .name("name")
                        .quantity(1L)
                        .price("10000")
                        .startTime(Instant.MIN)
                        .endTime(Instant.MAX)
                        .build()))
                .createdAt(instant)
                .build();

            final PaymentRequest paymentRequest = new PaymentRequest(orderId, userId, payType,
                isSuccess);

            when(orderConnector.findByOrderId(orderId)).thenReturn(Optional.of(order));

            // when
            final Exception exception = catchException(() -> paymentService.pay(paymentRequest));

            // then
            assertThat(exception).isInstanceOf(PayUserNotMatchException.class);
        }
    }
}
