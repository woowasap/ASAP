package shop.woowasap.shop.service.support.valid;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.service.dto.ProductResponse;

public final class ProductValidator {

    private ProductValidator() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"ProductValidator()\"");
    }

    public static void assertProductResponse(final ProductResponse result, final ProductResponse expected) {
        assertThat(result).isEqualTo(expected);
    }

}
