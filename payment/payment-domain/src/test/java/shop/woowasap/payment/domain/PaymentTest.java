package shop.woowasap.payment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.payment.domain.support.PaymentFixture;

@DisplayName("Payment 클래스")
class PaymentTest {

    @Nested
    @DisplayName("changeStatus 메소드는")
    class changeStatusMethod {

        @Test
        @DisplayName("PayStatus를 받아서, 상태를 변경한다.")
        void changeStatusWhenReceivePayStatus() {
            // given
            final Payment payment = PaymentFixture.defaultPayment();

            // when
            final Payment result = payment.changeStatus(PayStatus.CANCELED);

            // then
            assertThat(result.getPayStatus()).isEqualTo(PayStatus.CANCELED);
        }
    }
}