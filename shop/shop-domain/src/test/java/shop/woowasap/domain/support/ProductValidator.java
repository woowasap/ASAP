package shop.woowasap.domain.support;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.assertj.core.api.SoftAssertions;
import shop.woowasap.shop.domain.Product;

public class ProductValidator {

    private ProductValidator() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"ProductValidator()\"");
    }

    public static void assertProduct(
        long id,
        String name,
        String description,
        String price,
        int quantity,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Product update
    ) {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(update.getId()).isEqualTo(id);
            softAssertions.assertThat(update.getName()).isEqualTo(name);
            softAssertions.assertThat(update.getDescription()).isEqualTo(description);
            softAssertions.assertThat(update.getPrice()).isEqualTo(price);
            softAssertions.assertThat(update.getQuantity()).isEqualTo(quantity);

            softAssertions.assertThat(update.getStartTime())
                .isEqualTo(startTime.atZone(ZoneOffset.UTC).toInstant());
            softAssertions.assertThat(update.getEndTime())
                .isEqualTo(endTime.atZone(ZoneOffset.UTC).toInstant());
        });
    }

}
