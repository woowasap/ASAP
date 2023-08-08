package shop.woowasap.domain.product;

import static shop.woowasap.domain.support.DomainFixture.getDefaultBuilder;
import static shop.woowasap.domain.support.ProductValidator.assertProduct;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.Product;

@DisplayName("Product 클래스")
public class ProductTest {

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
            final int quantity = 8;
            final LocalDateTime startTime = LocalDateTime.of(2023, 8, 5, 11, 30);
            final LocalDateTime endTime = LocalDateTime.of(2023, 9, 5, 14, 30);

            // when
            Product update = original.update(name, description, price, quantity, startTime, endTime);

            // then
            assertProduct(original.getId(), name, description, price, quantity, startTime, endTime, update);
        }
    }
}
