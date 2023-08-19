package shop.woowasap.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.order.domain.out.event.StockFailEvent;
import shop.woowasap.order.domain.out.event.StockSuccessEvent;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.exception.DoesNotFindPaymentException;
import shop.woowasap.payment.domain.out.PaymentRepository;

@RecordApplicationEvents
@ExtendWith(SpringExtension.class)
@DisplayName("PaymentService 클래스")
@ContextConfiguration(classes = PaymentEventService.class)
class PaymentEventServiceTest {


    @Autowired
    private PaymentEventService paymentEventService;

    @MockBean
    private PaymentRepository paymentRepository;

    private Instant instant = Instant.parse("2023-08-15T00:00:02.00Z");

    @Nested
    @DisplayName("successPayment 메소드")
    class SuccessPaymentMethod {

        @Test
        @DisplayName("재고 처리 성공 이벤트 받아서 결제 상태 성공으로 변환")
        void successPaymentSuccess() {
            // given
            final Payment payment = Payment.builder()
                .paymentId(123L)
                .orderId(1234L)
                .userId(12345L)
                .payStatus(PayStatus.PENDING)
                .payType(PayType.DEPOSIT)
                .purchasedMoney(BigInteger.valueOf(10000L))
                .createdAt(instant)
                .build();

            final StockSuccessEvent event = new StockSuccessEvent(1234L);

            when(paymentRepository.findAllByOrderId(1234L)).thenReturn(List.of(payment));

            // when
            assertThatCode(() -> paymentEventService.successPayment(event))
                .doesNotThrowAnyException();

            // then
            verify(paymentRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("재고 처리 성공 이벤트 받아서 결제 상태 조회 실패시 예외 발생")
        void successPaymentPaymentNotFoundThenThrow() {
            // given
            final StockSuccessEvent event = new StockSuccessEvent(1234L);

            when(paymentRepository.findAllByOrderId(1234L)).thenReturn(List.of());

            // when
            Exception exception = catchException(() -> paymentEventService.successPayment(event));

            // then
            assertThat(exception).isInstanceOf(DoesNotFindPaymentException.class);
        }
    }


    @Nested
    @DisplayName("cancelPayment 메소드")
    class CancelPaymentMethod {

        @Test
        @DisplayName("재고 실패 성공 이벤트 받아서 결제 상태 실패로 변환")
        void cancelPaymentSuccess() {
            // given
            final Payment payment = Payment.builder()
                .paymentId(123L)
                .orderId(1234L)
                .userId(12345L)
                .payStatus(PayStatus.PENDING)
                .payType(PayType.DEPOSIT)
                .purchasedMoney(BigInteger.valueOf(10000L))
                .createdAt(instant)
                .build();

            final StockFailEvent event = new StockFailEvent(1234L);

            when(paymentRepository.findAllByOrderId(1234L)).thenReturn(List.of(payment));

            // when
            assertThatCode(() -> paymentEventService.cancelPayment(event))
                .doesNotThrowAnyException();

            // then
            verify(paymentRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("재고 처리 실패 이벤트 받아서 결제 상태 조회 실패시 예외 발생")
        void successPaymentPaymentNotFoundThenThrow() {
            // given
            final StockFailEvent event = new StockFailEvent(1234L);

            when(paymentRepository.findAllByOrderId(1234L)).thenReturn(List.of());

            // when
            Exception exception = catchException(() -> paymentEventService.cancelPayment(event));

            // then
            assertThat(exception).isInstanceOf(DoesNotFindPaymentException.class);
        }
    }

}
