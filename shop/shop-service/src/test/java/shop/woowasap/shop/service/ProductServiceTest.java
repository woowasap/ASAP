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
import shop.woowasap.shop.domain.api.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.api.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.api.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.api.product.response.ProductsResponse;
import shop.woowasap.shop.domain.exception.CannotFindProductException;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.domain.spi.ProductRepository;
import shop.woowasap.shop.domain.spi.response.ProductsPaginationResponse;
import shop.woowasap.shop.service.support.fixture.ProductDtoFixture;
import shop.woowasap.shop.service.support.fixture.ProductFixture;

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
            final Product product = productBuilder(noExistProductId).build();

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
    @DisplayName("getById 메소드는")
    class GetById_Method {

        @Test
        @DisplayName("id에 해당하는 Product 를 찾을 수 없을경우, CannotFindProductException 을 던진다.")
        void throwCannotFindProductExceptionWhenCannotFindIdMatchedProduct() {
            // given
            final long productId = 1L;

            when(productRepository.findByIdAndValidSaleTime(productId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> productService.getByProductId(productId));

            // then
            assertThat(exception).isInstanceOf(CannotFindProductException.class);
        }

        @Test
        @DisplayName("id에 해당하는 Product 가 존재한다면, ProductResponse를 반환한다.")
        void ReturnProductResponseWhenExistsIdMatchedProduct() {
            // given
            final long productId = 1L;
            final Product product = productBuilder(productId).build();
            final ProductDetailsResponse expected = ProductDtoFixture.fromProduct(product);

            when(productRepository.findByIdAndValidSaleTime(productId)).thenReturn(Optional.of(product));

            // when
            ProductDetailsResponse result = productService.getByProductId(productId);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
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
            assertThat(productsResponse).usingRecursiveComparison()
                .isEqualTo(expectedProductsResponse);
        }
    }

    @Nested
    @DisplayName("getValidProducts 메서드는")
    class GetValidProducts_Method {

        @Test
        @DisplayName("endTime 이 현재 시간보다 이후인 상품들을 반환한다")
        void returnValidProducts() {
            // given
            final int page = 1;
            final int pageSize = 4;
            final int totalPage = 1;

            Product product1 = ProductFixture.validProduct(1L);
            Product product2 = ProductFixture.validProduct(2L);

            List<Product> products = List.of(product1, product2);

            when(productRepository.findAllValidWithPagination(page, pageSize)).thenReturn(
                new ProductsPaginationResponse(products, page, totalPage));

            // when
            ProductsResponse result = productService.getValidProducts(page, pageSize);

            // then
            ProductsResponse expected = new ProductsResponse(
                ProductFixture.productsOfProductsResponse(products), page, totalPage);

            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("getByIdWithAdmin 메소드는")
    class GetByIdWithAdmin_Method {

        @Test
        @DisplayName("productId에 해당하는 Product 를 찾을 수 없을 경우, CannotFindProductException 을 던진다.")
        void throwCannotFindProductExceptionWhenCannotFindIdMatchedProduct() {
            // given
            final long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // when
            final Exception exception = catchException(
                () -> productService.getByProductIdWithAdmin(productId));

            // then
            assertThat(exception).isInstanceOf(CannotFindProductException.class);
        }

        @Test
        @DisplayName("productId에 해당하는 Product 가 존재한다면, ProductResponse를 반환한다.")
        void ReturnProductResponseWhenExistsIdMatchedProduct() {
            // given
            final long productId = 1L;
            final Product product = productBuilder(productId).build();
            final ProductDetailsResponse expected = ProductDtoFixture.fromProduct(product);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            final ProductDetailsResponse result = productService.getByProductIdWithAdmin(productId);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
