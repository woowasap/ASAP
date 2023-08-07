package shop.woowasap.domain.support;

import java.time.ZoneOffset;
import org.assertj.core.api.SoftAssertions;
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.dto.UpdateProductRequest;

public class ProductValidator {

    private ProductValidator() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"ProductValidator()\"");
    }

    public static void assertProduct(long id, UpdateProductRequest updateProductRequest, Product update) {
        SoftAssertions.assertSoftly(softAssertions -> {

            softAssertions.assertThat(update.getId()).isEqualTo(id);
            softAssertions.assertThat(update.getName()).isEqualTo(updateProductRequest.name());
            softAssertions.assertThat(update.getPrice()).isEqualTo(updateProductRequest.price());
            softAssertions.assertThat(update.getQuantity()).isEqualTo(updateProductRequest.quantity());
            softAssertions.assertThat(update.getDescription()).isEqualTo(updateProductRequest.description());

            softAssertions.assertThat(update.getStartTime())
                .isEqualTo(updateProductRequest.startTime()
                    .atZone(ZoneOffset.UTC).toInstant());
            softAssertions.assertThat(update.getEndTime())
                .isEqualTo(updateProductRequest.endTime()
                    .atZone(ZoneOffset.UTC).toInstant());
        });
    }

}
