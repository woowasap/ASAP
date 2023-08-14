package shop.woowasap.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.order.domain.exception.InvalidPriceException;
import shop.woowasap.order.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.order.domain.exception.InvalidQuantityException;
import shop.woowasap.order.domain.support.fixture.OrderProductFixture;

@DisplayName("OrderProduct 클래스")
class OrderProductTest {

    @Nested
    @DisplayName("new constructor 는")
    class newConstructor {

        @Test
        @DisplayName("상품 정보와 수량을 받으면 생성에 성공한다.")
        void createSuccessWhenReceiveProductInfoAndQuantity() {
            // given
            final long productId = 1L;
            final int quantity = 2;
            final String price = "10000";
            final Instant startTime = Instant.now().minusSeconds(100);
            final Instant endTime = Instant.now().plusSeconds(100);

            // when
            final Exception exception = catchException(() -> OrderProduct.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .price(price)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build());

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("price가 null이라면 InvalidPriceException을 던진다.")
        void throwInvalidPriceExceptionWhenPriceWasNull() {
            // given
            final String nullPrice = null;
            final OrderProduct.OrderProductBuilder defaultOrderProductBuilder = OrderProductFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> defaultOrderProductBuilder
                    .price(nullPrice)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidPriceException.class);
        }

        @Test
        @DisplayName("price가 NumberFormat이 아니라면, InvalidPriceException을 던진다.")
        void throwInvalidPriceExceptionWhenPriceDoesNotNumberFormat() {
            // given
            final String invalidPrice = "Not_Number";
            final OrderProduct.OrderProductBuilder defaultOrderProductBuilder = OrderProductFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> defaultOrderProductBuilder
                    .price(invalidPrice)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidPriceException.class);
        }

        @Test
        @DisplayName("quantity가 0 이하라면, InvalidQuantityException을 던진다.")
        void throwInvalidQuantityExceptionWhenQuantityUnderZero() {
            // given
            final int zeroQuantity = 0;
            final OrderProduct.OrderProductBuilder defaultOrderProductBuilder = OrderProductFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> defaultOrderProductBuilder
                    .quantity(zeroQuantity)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidQuantityException.class);
        }

        @Test
        @DisplayName("startTime > currentTime 이라면, InvalidProductSaleTimeException을 던진다.")
        void throwInvalidProductSaleTimeExceptionWhenStartTimeIsAfterCurrentTime() {
            // given
            final Instant invalidStartTime = Instant.now().plusSeconds(100);
            final OrderProduct.OrderProductBuilder orderProductBuilder = OrderProductFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> orderProductBuilder.startTime(invalidStartTime)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }

        @Test
        @DisplayName("endTime < currentTime 이라면, InvalidProductSaleTimeException을 던진다.")
        void throwInvalidProductSaleTimeExceptionWhenEndTimeIsBeforeCurrentTime() {
            // given
            final Instant invalidEndTime = Instant.now().minusSeconds(100);
            final OrderProduct.OrderProductBuilder orderProductBuilder = OrderProductFixture.defaultBuilder();

            // when
            final Exception exception = catchException(() -> orderProductBuilder.endTime(invalidEndTime)
                    .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }
    }
}
