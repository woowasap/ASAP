package shop.woowasap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;
import static shop.woowasap.accept.product.ProductFixture.updateProductRequest;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woowasap.shop.dto.UpdateProductRequest;
import shop.woowasap.shop.service.ProductService;
import shop.woowasap.shop.service.exception.UpdateProductException;
import shop.woowasap.shop.service.repository.ProductRepository;

@DisplayName("ProductService 클래스")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("update 메소드는")
    class Update_Method {

        @Test
        @DisplayName("ProductId 에 해당하는 Product 가 존재하지 않을 경우 UpdateProductException 을 던진다")
        void throwUpdateProductExceptionWhenNoProductExist() {
            // given
            final long noExistProductId = 1L;
            final UpdateProductRequest updateProductRequest = updateProductRequest();

            when(productRepository.findById(noExistProductId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> productService.update(noExistProductId, updateProductRequest));

            // then
            assertThat(exception).isInstanceOf(UpdateProductException.class);
            assertThat(exception.getMessage()).contains("productId 에 해당하는 Product 가 존재하지 않습니다.");

        }

    }


}
