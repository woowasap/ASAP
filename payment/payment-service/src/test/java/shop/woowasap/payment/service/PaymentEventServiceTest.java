package shop.woowasap.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.order.domain.out.event.OrderCanceledEvent;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.exception.DoesNotFindPaymentException;
import shop.woowasap.payment.domain.out.PaymentRepository;
import shop.woowasap.payment.service.support.PaymentFixture;

@ExtendWith(SpringExtension.class)
@DisplayName("PaymentEventServiceTest 클래스")
@ContextConfiguration(classes = PaymentEventService.class)
class PaymentEventServiceTest {

    @Autowired
    private PaymentEventService paymentEventService;

    @MockBean
    private PaymentRepository paymentRepository;

    @Nested
    @DisplayName("cancelPay 메소드는")
    class cancelPayMethod {

        @Test
        @DisplayName("OrderCanceledEvent 를 받아, 주문id에 해당하는 결제를 취소한다.")
        void cancelPayWhenReceiveOrderCanceledEvent() {
            // given
            final OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(1L);

            when(paymentRepository.findByOrderId(orderCanceledEvent.orderId())).thenReturn(
                Optional.of(PaymentFixture.defaultPayment()));

            // when
            final Exception result = catchException(
                () -> paymentEventService.cancelPay(orderCanceledEvent));

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("orderId에 해당하는 Pay를 찾을 수 없는경우, DoesNotFindPaymentException를 던진다.")
        void throwDoesNotFindPaymentExceptionWhenCannotFindPayByOrderId() {
            // given
            final OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent(1L);

            when(paymentRepository.findByOrderId(orderCanceledEvent.orderId())).thenReturn(
                Optional.empty());

            // when
            final Exception result = catchException(
                () -> paymentEventService.cancelPay(orderCanceledEvent));

            // then
            assertThat(result).isInstanceOf(DoesNotFindPaymentException.class);
        }
    }
}
