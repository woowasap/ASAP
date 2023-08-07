package shop.woowasap.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static shop.woowasap.accept.product.ProductFixture.updateProductRequest;
import static shop.woowasap.domain.product.DomainFixture.getDefaultBuilder;

import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
            UpdateProductRequest updateProductRequest = updateProductRequest();
            Product original = getDefaultBuilder().build();

            // when
            Product update = original.update(original.getId(), updateProductRequest);

            // then
            assertThat(update.getId()).isEqualTo(original.getId());
            assertThat(update.getName()).isEqualTo(updateProductRequest.name());
            assertThat(update.getPrice()).isEqualTo(updateProductRequest.price());
            assertThat(update.getQuantity()).isEqualTo(updateProductRequest.quantity());
            assertThat(update.getDescription()).isEqualTo(updateProductRequest.description());

            assertThat(update.getStartTime()).isEqualTo(updateProductRequest.startTime()
                .atZone(ZoneOffset.UTC).toInstant());
            assertThat(update.getEndTime()).isEqualTo(updateProductRequest.endTime()
                .atZone(ZoneOffset.UTC).toInstant());

        }
    }
}
