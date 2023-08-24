package shop.woowasap.payment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.BeanScanBaseLocation;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.repository.jpa.PaymentEntityRepository;

@DataJpaTest
@DisplayName("PaymentRepositoryImpl 클래스")
@ContextConfiguration(classes = {BeanScanBaseLocation.class, PaymentRepositoryImpl.class})
class PaymentRepositoryImplTest {

    @Autowired
    private PaymentRepositoryImpl paymentRepository;

    @Autowired
    private PaymentEntityRepository paymentEntityRepository;

    @Nested
    @DisplayName("create 메소드")
    class createMethod {

        @Test
        @DisplayName("정상 입력 시 데이터 베이스에 저장됨")
        void saveSuccess() {
            // given
            final Instant instant = Instant.parse("2023-08-15T00:00:02.00Z");
            final Payment payment = Payment.builder()
                .paymentId(1L)
                .orderId(12L)
                .userId(123L)
                .purchasedMoney(BigInteger.valueOf(10000L))
                .payType(PayType.CARD)
                .payStatus(PayStatus.SUCCESS)
                .createdAt(instant)
                .build();

            // when
            final Payment result = paymentRepository.create(payment);

            // then
            assertThat(result).usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(payment);
        }

    }

    @Nested
    @DisplayName("persist 메소드")
    class persistMethod {

        @Test
        @DisplayName("Payment의 상태를 변경한다")
        void saveSuccess() {
            // given
            final Instant instant = Instant.parse("2023-08-15T00:00:02.00Z");
            final Payment payment = Payment.builder()
                .paymentId(1L)
                .orderId(12L)
                .userId(123L)
                .purchasedMoney(BigInteger.valueOf(10000L))
                .payType(PayType.CARD)
                .payStatus(PayStatus.SUCCESS)
                .createdAt(instant)
                .build();

            paymentRepository.create(payment);
            payment.changeStatus(PayStatus.CANCELED);

            // when
            final Payment result = paymentRepository.persist(payment);

            // then
            assertThat(result).usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(payment);
        }

    }

    @Nested
    @DisplayName("findByOrderId 메소드")
    class FindByOrderIdMethod {

        @Test
        @DisplayName("정상 입력시 객체 반환")
        void findSuccess() {
            // given
            final Instant instant = Instant.parse("2023-08-15T00:00:02.00Z");
            final Payment payment = Payment.builder()
                .paymentId(1L)
                .orderId(12L)
                .userId(123L)
                .purchasedMoney(BigInteger.valueOf(10000L))
                .payType(PayType.CARD)
                .payStatus(PayStatus.SUCCESS)
                .createdAt(instant)
                .build();

            paymentRepository.create(payment);

            // when
            final Optional<Payment> result = paymentRepository.findByOrderId(12L);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(payment);
        }
    }
}
