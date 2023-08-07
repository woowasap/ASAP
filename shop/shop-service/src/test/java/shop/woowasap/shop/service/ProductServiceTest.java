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
import shop.woowasap.shop.domain.Product;
import shop.woowasap.shop.service.dto.ProductResponse;
import shop.woowasap.shop.service.exception.CannotFindProductException;
import shop.woowasap.shop.service.repository.ProductRepository;
import shop.woowasap.shop.service.support.fixture.ProductFixture;
import shop.woowasap.shop.service.support.valid.ProductValidator;

@ExtendWith(SpringExtension.class)
@DisplayName("ProductService 클래스")
@ContextConfiguration(classes = ProductService.class)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Nested
    @DisplayName("getProductById 메소드는")
    class GetProductByIdMethod {

        @Test
        @DisplayName("id에 해당하는 product를 찾을 수 없으면, CannotFindProductException을 던진다.")
        void throwCannotFindProductExceptionWhenNoProductExist() {
            // given
            final long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> productService.getProductById(productId));

            // then
            assertThat(exception).isInstanceOf(CannotFindProductException.class);
        }

        @Test
        @DisplayName("id에 해당하는 product를 찾으면, ProductResponse를 응답한다.")
        void returnProductResponseWhenMatchedIdProduct() {
            // given
            final Product product = ProductFixture.defaultBuilder().build();
            final ProductResponse expected = ProductFixture.toProductResponse(product, "Asia/Seoul");

            when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

            // when
            ProductResponse result = productService.getProductById(product.getId());

            // then
            ProductValidator.assertProductResponse(result, expected);
        }
    }

}
