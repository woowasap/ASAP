package shop.woowasap.shop.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchException;
import static shop.woowasap.shop.domain.support.ProductFixture.getDefaultBuilder;
import static shop.woowasap.shop.domain.support.ProductFixture.getProductWithSaleTime;

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
import shop.woowasap.shop.domain.exception.InvalidProductSaleDurationException;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.shop.domain.exception.InvalidProductStartTimeException;
import shop.woowasap.shop.domain.exception.ProductModificationPermissionException;

@DisplayName("Product 테스트")
class ProductTest {

    @Nested
    @DisplayName("Product Builder 는 생성 시")
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
        @DisplayName("saleTime 이 null 인 경우, InvalidProductSaleTimeException 을 반환한다.")
        void throwExceptionWhenNullSaleTime() {
            // given
            final SaleTime saleTime = null;

            // when
            final Exception exception = catchException(
                () -> getDefaultBuilder().saleTime(saleTime).build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }

        @Test
        @DisplayName("판매 시작 시간이 판매 종료 시간과 동일한 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenStartTimeEqualsEndTime() {
            // given
            final Instant sameTime = Instant.parse("2023-08-05T20:10:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            // when
            final Exception exception = catchException(
                () -> getProductWithSaleTime(sameTime, sameTime, nowTime));

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleDurationException.class);
        }

        @Test
        @DisplayName("판매 종료 시간이 판매 시작 시간보다 빠른 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenEndTimeLessThanStartTime() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:10:00.000Z");
            final Instant endTime = Instant.parse("2023-08-05T19:10:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            // when
            final Exception exception = catchException(
                () -> getProductWithSaleTime(startTime, endTime, nowTime));

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }

        @Test
        @DisplayName("판매 시작 시간이 현재 시간으로부터 10분 이전이라 InvalidProductStartTimeException을 반환한다.")
        void throwExceptionWhenStartTimeIn10MinutesFromNow() {
            // given
            final Instant startTime = Instant.parse("2023-08-01T00:05:00.000Z");
            final Instant endTime = Instant.parse("2023-08-01T19:02:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            // when
            final Exception exception = catchException(
                () -> getProductWithSaleTime(startTime, endTime, nowTime));

            // then
            assertThat(exception).isInstanceOf(InvalidProductStartTimeException.class);
        }

        @Test
        @DisplayName("판매 시간이 1시간 미만인 경우, InvalidProductSaleDurationException 을 반환한다.")
        void throwExceptionWhenDurationIsUnder1Hour() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:10:00.000Z");
            final Instant endTime = Instant.parse("2023-08-05T20:15:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            // when
            final Exception exception = catchException(
                () -> getProductWithSaleTime(startTime, endTime, nowTime));

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleDurationException.class);
        }

        @Test
        @DisplayName("판매 시간이 10일을 초과하는 경우, InvalidProductSaleDurationException 반환한다.")
        void throwExceptionWhenDurationIsOver12Hour() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:00:00.000Z");
            final Instant endTime = Instant.parse("2023-08-20T20:00:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            // when
            final Exception exception = catchException(
                () -> getProductWithSaleTime(startTime, endTime, nowTime));

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleDurationException.class);
        }

    }

    @Nested
    @DisplayName("update 메소드는")
    class Update_Method {

        @Test
        @DisplayName("갱신된 Product 를 반환한다")
        void returnUpdatedProduct() {
            // given
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");
            final Product original = getDefaultBuilder().build();

            final String name = "newProductName";
            final String description = "newProductDescription";
            final String price = "100";
            final long quantity = 8;
            final LocalDateTime startTime = LocalDateTime.of(2023, 8, 5, 11, 30);
            final LocalDateTime endTime = LocalDateTime.of(2023, 8, 5, 14, 30);
            final Instant updateTime = nowTime.plusSeconds(60 * 10);

            final SaleTime saleTime = SaleTime.builder()
                .startTime(startTime.atZone(ZoneOffset.UTC).toInstant())
                .endTime(endTime.atZone(ZoneOffset.UTC).toInstant())
                .build();

            Product expected = Product.builder()
                .id(original.getId())
                .name(name).description(description)
                .price(price)
                .quantity(quantity)
                .saleTime(saleTime)
                .build();

            // when
            final Product update = original.update(
                name,
                description,
                price,
                quantity,
                SaleTime.builder()
                    .startTime(startTime.toInstant(ZoneOffset.UTC))
                    .endTime(endTime.toInstant(ZoneOffset.UTC))
                    .build(),
                updateTime
            );

            // then
            assertThat(update).usingRecursiveAssertion().isEqualTo(expected);
        }

        @Test
        @DisplayName("Product 가 현재 판매중일 경우, ProductModificationPermissionException 를 던진다.")
        void throwProductModificationPermissionExceptionWhenProductIsOnSale() {
            // given
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            final SaleTime saleTime = SaleTime.builder()
                .startTime(nowTime.plusSeconds(60 * 60))
                .endTime(nowTime.plusSeconds(60 * 60 * 3))
                .nowTime(nowTime)
                .build();

            final Product original = getDefaultBuilder()
                .saleTime(saleTime)
                .build();

            final String name = "newProductName";
            final String description = "newProductDescription";
            final String price = "100";
            final long quantity = 8;
            final LocalDateTime startTime = LocalDateTime.of(2030, 8, 5, 11, 0);
            final LocalDateTime endTime = LocalDateTime.of(2030, 8, 5, 12, 0);
            final Instant updateTime = nowTime.plusSeconds(60 * 60 * 2);

            // when
            final Exception exception = catchException(
                () -> original.update(
                    name,
                    description,
                    price,
                    quantity,
                    SaleTime.builder()
                        .startTime(startTime.toInstant(ZoneOffset.UTC))
                        .endTime(endTime.toInstant(ZoneOffset.UTC))
                        .build(),
                    updateTime));

            // then
            assertThat(exception).isInstanceOf(ProductModificationPermissionException.class);
        }
    }

    @Nested
    @DisplayName("isEndTimeBefore 메서드는")
    class IsEndTimeBefore_Method {

        @Test
        @DisplayName("EndTime 이 time 이전이라면 true 를 반환한다.")
        void returnTrueWhenEndTimeIsBeforeTime() {
            // given
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            final SaleTime saleTime = SaleTime.builder()
                .startTime(nowTime.plusSeconds(60 * 60))
                .endTime(nowTime.plusSeconds(60 * 60 * 2))
                .nowTime(nowTime)
                .build();

            final Product product = getDefaultBuilder()
                .saleTime(saleTime)
                .build();

            final Instant time = nowTime.plusSeconds(60 * 60 * 3);

            // when
            final boolean result = product.isEndTimeBefore(time);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("EndTime 이 time 이후라면 false 를 반환한다.")
        void returnFalseWhenEndTimeIsAfterTime() {
            // given
            final Instant nowTime = Instant.parse("2023-08-01T00:00:00.000Z");

            final SaleTime saleTime = SaleTime.builder()
                .startTime(nowTime.plusSeconds(60 * 60))
                .endTime(nowTime.plusSeconds(60 * 60 * 2))
                .nowTime(nowTime)
                .build();

            final Product product = getDefaultBuilder()
                .saleTime(saleTime)
                .build();

            final Instant time = nowTime.plusSeconds(60 * 60);

            // when
            final boolean result = product.isEndTimeBefore(time);

            // then
            assertThat(result).isFalse();
        }
    }

}
