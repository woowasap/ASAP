package shop.woowasap.shop.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static shop.woowasap.shop.domain.support.DomainFixture.getDefaultBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import shop.woowasap.shop.domain.exception.InvalidProductDescriptionException;
import shop.woowasap.shop.domain.exception.InvalidProductNameException;
import shop.woowasap.shop.domain.exception.InvalidProductPriceException;
import shop.woowasap.shop.domain.exception.InvalidProductQuantityException;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;

@DisplayName("Product 테스트")
class ProductTest {

    @Nested
    @DisplayName("Product를 생성 시")
    class newProductWithBuilder {

        @Test
        @DisplayName("정상적인 입력인 경우 상품을 생성한다.")
        void createProduct() {
            // when & then
            assertThatCode(() -> getDefaultBuilder().build())
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("잘못된 상품 이름이 입력되는 경우, ProductException을 반환한다.")
        void throwExceptionWhenInvalidProductName(final String invalidName) {
            // when
            final Exception exception = catchException(
                () -> getDefaultBuilder().name(invalidName).build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductNameException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("잘못된 상품 설명이 입력되는 경우, ProductException을 반환한다.")
        void throwExceptionWhenInvalidProductDescription(final String invalidDescription) {
            // when
            final Exception exception = catchException(
                () -> getDefaultBuilder().description(invalidDescription).build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductDescriptionException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"-1", "0", "0000000", "Hello"})
        @DisplayName("잘못된 상품 가격이 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenInvalidProductPrice(final String invalidPrice) {
            // when
            final Exception exception = catchException(
                () -> getDefaultBuilder().price(invalidPrice).build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductPriceException.class);
        }

        @Test
        @DisplayName("잘못된 상품 수량이 음수로 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenNegativeQuantity() {
            // given
            final Long negativeQuantity = -1L;

            // when
            final Exception exception = catchException(
                () -> getDefaultBuilder().quantity(negativeQuantity).build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductQuantityException.class);
        }

        @Test
        @DisplayName("잘못된 상품 수량이 null로 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenNullQuantity() {
            // given
            final Long nullQuantity = null;

            // when
            final Exception exception = catchException(
                () -> getDefaultBuilder().quantity(nullQuantity).build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductQuantityException.class);
        }

        @Test
        @DisplayName("판매 시작 시간이 판매 종료 시간과 동일한 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenStartTimeEqualsEndTime() {
            // given
            final Instant sameTime = Instant.parse("2023-08-05T20:10:00.000Z");

            // when
            final Exception exception = catchException(() -> getDefaultBuilder()
                .startTime(sameTime)
                .endTime(sameTime)
                .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }

        @Test
        @DisplayName("판매 종료 시간이 판매 시작 시간보다 빠른 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenEndTimeLessThanStartTime() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:10:00.000Z");
            final Instant lessThanStartTime = Instant.parse("2023-08-05T19:10:00.000Z");

            // when
            final Exception exception = catchException(() -> getDefaultBuilder()
                .startTime(startTime)
                .endTime(lessThanStartTime)
                .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Update_Method {

        @Test
        @DisplayName("갱신된 Product 를 반환한다")
        void returnUpdatedProduct() {
            // given
            final Product original = getDefaultBuilder().build();

            final String name = "newProductName";
            final String description = "newProductDescription";
            final String price = "100";
            final long quantity = 8;
            final LocalDateTime startTime = LocalDateTime.of(2023, 8, 5, 11, 30);
            final LocalDateTime endTime = LocalDateTime.of(2023, 9, 5, 14, 30);

            // when
            Product update = original.update(name, description, price, quantity, startTime,
                endTime);

            // then
            Product expected = Product.builder()
                .id(original.getId())
                .name(name).description(description)
                .price(price)
                .quantity(quantity)
                .startTime(startTime.atZone(ZoneOffset.UTC).toInstant())
                .endTime(endTime.atZone(ZoneOffset.UTC).toInstant())
                .build();

            assertThat(update).usingRecursiveAssertion().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("isEndTimeBefore 메서드는")
    class IsEndTimeBefore_Method {

        @Test
        @DisplayName("EndTime 이 time 이전이라면 true 를 반환한다.")
        void returnTrueWhenEndTimeIsBeforeTime() {
            // given
            final Product product = getDefaultBuilder()
                .startTime(Instant.now().minusSeconds(1_000))
                .endTime(Instant.now().plusSeconds(1_000))
                .build();

            final Instant time = Instant.now().plusSeconds(10_000);

            // when
            final boolean result = product.isEndTimeBefore(time);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("EndTime 이 time 이후라면 false 를 반환한다.")
        void returnFalseWhenEndTimeIsAfterTime() {
            // given
            final Product product = getDefaultBuilder()
                .startTime(Instant.now().minusSeconds(1_000))
                .endTime(Instant.now().plusSeconds(1_000))
                .build();

            final Instant time = Instant.now();

            // when
            final boolean result = product.isEndTimeBefore(time);

            // then
            assertThat(result).isFalse();
        }
    }

}
