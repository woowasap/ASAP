package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.shop.app.api.ProductConnector;
import shop.woowasap.shop.app.exception.CannotFindProductException;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.spi.ProductRepository;
import shop.woowasap.shop.service.support.fixture.ProductFixture;

@ExtendWith(SpringExtension.class)
@DisplayName("ProductConnectorService 클래스")
@ContextConfiguration(classes = ProductConnectorService.class)
class ProductConnectorServiceTest {

    @Autowired
    private ProductConnector productConnector;

    @MockBean
    private ProductRepository productRepository;

    @Nested
    @DisplayName("getByProductId 메소드는")
    class getByProductIdMethod {

        @Test
        @DisplayName("productId를 받아 Product를 반환한다.")
        void returnProductWhenReceiveProductId() {
            // given
            final long productId = 1L;
            final Product expected = ProductFixture.validProduct(productId);

            when(productRepository.findById(productId))
                .thenReturn(Optional.of(ProductFixture.validProduct(productId)));

            // when
            final Product result = productConnector.getByProductId(productId);

            // then
            assertThat(result).usingRecursiveAssertion()
                .ignoringFieldsMatchingRegexes("id")
                .isEqualTo(expected);
        }

        @Test
        @DisplayName("productId에 해당하는 Product를 찾을 수 없으면, CannotFindProductException를 던진다.")
        void throwCannotFindProductExceptionWhenCannotFindMatchedProduct() {
            // given
            final long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // when
            final Exception result = catchException(() -> productConnector.getByProductId(productId));

            // then
            assertThat(result).isInstanceOf(CannotFindProductException.class);
        }
    }
}
