package shop.woowasap.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static shop.woowasap.accept.product.ProductFixture.updateProductRequest;
import static shop.woowasap.domain.product.DomainFixture.getDefaultBuilder;
import static shop.woowasap.domain.validate.ProductValidator.assertProduct;

import java.time.ZoneOffset;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.domain.validate.ProductValidator;
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.dto.UpdateProductRequest;

@DisplayName("Product 클래스")
public class ProductTest {

    @Nested
    @DisplayName("update 메소드는")
    class Update_Method {

        @Test
        @DisplayName("갱신된 Product 를 반환한다")
        void returnUpdatedProduct() {
            // given
            final UpdateProductRequest updateProductRequest = updateProductRequest();
            final Product original = getDefaultBuilder().build();

            // when
            final Product update = original.update(original.getId(), updateProductRequest);

            // then
            assertProduct(original.getId(), updateProductRequest, update);
        }
    }
}
