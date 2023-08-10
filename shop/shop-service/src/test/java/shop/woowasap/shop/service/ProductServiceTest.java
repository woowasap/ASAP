package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.productBuilder;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.productsResponse;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.registerProductRequest;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.updateProductRequest;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.shop.app.api.response.ProductsResponse;
import shop.woowasap.shop.app.product.Product;
import shop.woowasap.shop.app.api.request.RegisterProductRequest;
import shop.woowasap.shop.app.api.request.UpdateProductRequest;
import shop.woowasap.shop.app.exception.CannotFindProductException;
import shop.woowasap.shop.app.spi.ProductRepository;
import shop.woowasap.shop.app.spi.response.ProductsPaginationResponse;
import shop.woowasap.shop.service.support.fixture.DomainFixture;

@ExtendWith(SpringExtension.class)
@DisplayName("ProductService 클래스")
@ContextConfiguration(classes = ProductService.class)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private ProductRepository productRepository;

    @Nested
    @DisplayName("Product를 생성하는 메서드는")
    class CreateProduct {

        @Test
        @DisplayName("정상적인 Request 입력 시, Product를 생성한다.")
        void create() {
            // given
            final Long productId = 1L;
            final RegisterProductRequest registerProductRequest = registerProductRequest();
            final Product product = productBuilder(productId).build();
            when(productRepository.persist(any())).thenReturn(product);
            when(idGenerator.generate()).thenReturn(productId);

            // when
            final Long resultProductId = productService.registerProduct(registerProductRequest);

            // then
            assertThat(resultProductId).isEqualTo(productId);
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Update_Method {

        @Test
        @DisplayName("Product 를 갱신한다")
        void updateProduct() {
            // given
            final long noExistProductId = 1L;
            final UpdateProductRequest updateProductRequest = updateProductRequest();
            final Product product = DomainFixture.getDefaultBuilder().build();

            when(productRepository.findById(noExistProductId)).thenReturn(Optional.of(product));

            // when
            productService.update
                (noExistProductId, updateProductRequest);

            // then
            verify(productRepository).persist(any(Product.class));
        }

        @Test
        @DisplayName("ProductId 에 해당하는 Product 가 존재하지 않을 경우 CannotFindProductException 을 던진다")
        void throwUpdateProductExceptionWhenNoProductExist() {
            // given
            final long noExistProductId = 1L;
            final UpdateProductRequest updateProductRequest = updateProductRequest();

            when(productRepository.findById(noExistProductId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> productService.update(noExistProductId, updateProductRequest));

            // then
            assertThat(exception).isInstanceOf(CannotFindProductException.class);
            assertThat(exception.getMessage()).contains("productId 에 해당하는 Product 가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("getProductsInAdmin 메소드는")
    class getProductsInAdmin_Method {

        @Test
        @DisplayName("Product 들을 반환한다.")
        void getProductsInAdmin() {
            // given
            final int page = 1;
            final int totalPage = 1;
            final int size = 10;
            final List<Product> products = List.of(
                productBuilder(1L).build(), productBuilder(2L).build());

            when(productRepository.findAllWithPagination(page, size))
                .thenReturn(new ProductsPaginationResponse(products, page, totalPage));
            final ProductsResponse expectedProductsResponse = productsResponse(products);

            // when
            final ProductsResponse productsResponse = productService.getProductsInAdmin(page, size);

            // then
            assertThat(productsResponse).usingRecursiveComparison().isEqualTo(expectedProductsResponse);
        }
    }
}
