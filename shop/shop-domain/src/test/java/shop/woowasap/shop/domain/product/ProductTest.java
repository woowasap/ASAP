package shop.woowasap.shop.domain.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            assertDoesNotThrow(() -> Product.builder()
                .name("name")
                .description("description")
                .price("10000")
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .build()
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("잘못된 상품 이름이 입력되는 경우, ProductException을 반환한다.")
        void throwExceptionWhenInvalidProductName(final String productName) {
            assertThrows(InvalidProductNameException.class, () -> Product.builder()
                .name(productName)
                .description("description")
                .price("10000")
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .build()
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("잘못된 상품 설명이 입력되는 경우, ProductException을 반환한다.")
        void throwExceptionWhenInvalidProductDescription(final String description) {
            assertThrows(InvalidProductDescriptionException.class, () -> Product.builder()
                .name("name")
                .description(description)
                .price("10000")
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .build()
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"-1", "0", "0000000", "Hello"})
        @DisplayName("잘못된 상품 가격이 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenInvalidProductPrice(final String price) {
            assertThrows(InvalidProductPriceException.class, () -> Product.builder()
                .name("name")
                .description("description")
                .price(price)
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .build()
            );
        }

        @Test
        @DisplayName("잘못된 상품 수량이 입력되는 경우, InvalidProductPriceException을 반환한다.")
        void throwExceptionWhenInvalidQuantity() {
            final Long invalidQuantity = -1L;

            assertThrows(InvalidProductQuantityException.class, () -> Product.builder()
                .name("name")
                .description("description")
                .price("10000")
                .quantity(invalidQuantity)
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .build()
            );
        }

        @Test
        @DisplayName("판매 시작 시간이 판매 종료 시간과 동일한 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenStartTimeEqualsEndTime() {
            assertThrows(InvalidProductSaleTimeException.class, () -> Product.builder()
                .name("name")
                .description("description")
                .price("10000")
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .endTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .build()
            );
        }

        @Test
        @DisplayName("판매 종료 시간이 판매 시작 시간보다 빠른 경우, InvalidProductSaleTimeException을 반환한다.")
        void throwExceptionWhenEndTimeLessThanStartTime() {
            assertThrows(InvalidProductSaleTimeException.class, () -> Product.builder()
                .name("name")
                .description("description")
                .price("10000")
                .quantity(1000L)
                .startTime(Instant.parse("2023-08-05T20:10:00.000Z"))
                .endTime(Instant.parse("2023-08-05T19:10:00.000Z"))
                .build()
            );
        }
    }
}
