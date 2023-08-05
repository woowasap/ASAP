package shop.woowasap.shop.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static shop.woowasap.shop.domain.product.DomainFixture.ProductBuilder;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import shop.woowasap.shop.domain.product.exception.InvalidProductDescriptionException;
import shop.woowasap.shop.domain.product.exception.InvalidProductNameException;
import shop.woowasap.shop.domain.product.exception.InvalidProductPriceException;
import shop.woowasap.shop.domain.product.exception.InvalidProductQuantityException;
import shop.woowasap.shop.domain.product.exception.InvalidProductSaleTimeException;

@DisplayName("Product 테스트")
class ProductTest {

    @Nested
    @DisplayName("Product를 생성 시")
    class newProductWithBuilder {

        @Test
        @DisplayName("정상적인 입력인 경우 상품을 생성한다.")
        void createProduct() {
            assertThatCode(() -> ProductBuilder.getDefaultBuilder().build())
                .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("잘못된 상품 이름이 입력되는 경우, ProductException을 반환한다.")
        void throwExceptionWhenInvalidProductName(final String invalidName) {
            final Exception exception = catchException(
                () -> ProductBuilder.getDefaultBuilder().name(invalidName).build());

            assertThat(exception).isInstanceOf(InvalidProductNameException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("잘못된 상품 설명이 입력되는 경우, ProductException을 반환한다.")
        void throwExceptionWhenInvalidProductDescription(final String invalidDescription) {
            final Exception exception = catchException(
                () -> ProductBuilder.getDefaultBuilder().description(invalidDescription).build());

            assertThat(exception).isInstanceOf(InvalidProductDescriptionException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"-1", "0", "0000000", "Hello"})
        @DisplayName("잘못된 상품 가격이 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenInvalidProductPrice(final String invalidPrice) {
            final Exception exception = catchException(
                () -> ProductBuilder.getDefaultBuilder().price(invalidPrice).build());

            assertThat(exception).isInstanceOf(InvalidProductPriceException.class);
        }

        @Test
        @DisplayName("잘못된 상품 수량이 음수로 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenNegativeQuantity() {
            final Long negativeQuantity = -1L;

            final Exception exception = catchException(
                () -> ProductBuilder.getDefaultBuilder().quantity(negativeQuantity).build());

            assertThat(exception).isInstanceOf(InvalidProductQuantityException.class);
        }

        @Test
        @DisplayName("잘못된 상품 수량이 null로 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenNullQuantity() {
            final Long nullQuantity = null;

            final Exception exception = catchException(
                () -> ProductBuilder.getDefaultBuilder().quantity(nullQuantity).build());

            assertThat(exception).isInstanceOf(InvalidProductQuantityException.class);
        }

        @Test
        @DisplayName("판매 시작 시간이 판매 종료 시간과 동일한 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenStartTimeEqualsEndTime() {
            final Instant sameTime = Instant.parse("2023-08-05T20:10:00.000Z");

            final Exception exception = catchException(() -> ProductBuilder.getDefaultBuilder()
                .startTime(sameTime)
                .endTime(sameTime)
                .build());

            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }

        @Test
        @DisplayName("판매 종료 시간이 판매 시작 시간보다 빠른 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenEndTimeLessThanStartTime() {
            final Instant startTime = Instant.parse("2023-08-05T20:10:00.000Z");
            final Instant lessThanStartTime = Instant.parse("2023-08-05T19:10:00.000Z");

            final Exception exception = catchException(() -> ProductBuilder.getDefaultBuilder()
                .startTime(startTime)
                .endTime(lessThanStartTime)
                .build());

            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }
    }
}
