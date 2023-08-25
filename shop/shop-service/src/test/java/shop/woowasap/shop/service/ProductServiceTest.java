package shop.woowasap.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.afterSaleProduct;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.beforeSaleProduct;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.beforeSaleProductBuilder;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.onSaleProduct;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.productsResponse;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.registerProductRequest;
import static shop.woowasap.shop.service.support.fixture.ProductFixture.updateProductRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.core.util.time.TimeUtil;
import shop.woowasap.shop.domain.exception.NotExistsProductException;
import shop.woowasap.shop.domain.exception.ProductModificationPermissionException;
import shop.woowasap.shop.domain.exception.SaleEndedProductException;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;
import shop.woowasap.shop.domain.out.ProductRepository;
import shop.woowasap.shop.domain.out.response.ProductsPaginationAdminResponse;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.service.support.fixture.ProductDtoFixture;
import shop.woowasap.shop.service.support.fixture.ProductFixture;

@ExtendWith(SpringExtension.class)
@DisplayName("ProductService 클래스")
@ContextConfiguration(classes = ProductService.class)
class ProductServiceTest {

    private static final Instant NOW_TIME = Instant.parse("2023-08-01T01:00:00.000Z");

    @Autowired
    private ProductService productService;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private TimeUtil timeUtil;

    @BeforeEach
    void setUpTime() {
        when(timeUtil.now()).thenReturn(NOW_TIME);
    }

    @Nested
    @DisplayName("Product 를 생성하는 메서드는")
    class CreateProduct {

        @Test
        @DisplayName("정상적인 Request 입력 시, Product 를 생성한다.")
        void create() {
            // given
            final Long productId = 1L;
            final Product product = beforeSaleProduct(productId);
            final RegisterProductRequest registerProductRequest = registerProductRequest(product);

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
            final long productId = 1L;
            final Product product = beforeSaleProduct(productId);
            final UpdateProductRequest updateProductRequest = updateProductRequest(product);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            productService.update
                (productId, updateProductRequest);

            // then
            verify(productRepository).persist(any(Product.class));
        }

        @Test
        @DisplayName("ProductId 에 해당하는 Product 가 존재하지 않을 경우 NotExistsProductException 을 던진다")
        void throwUpdateProductExceptionWhenNoProductExist() {
            // given
            final long noExistProductId = 1L;
            final Product product = beforeSaleProduct(noExistProductId);
            final UpdateProductRequest updateProductRequest = updateProductRequest(product);

            when(productRepository.findById(noExistProductId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> productService.update(noExistProductId, updateProductRequest));

            // then
            assertThat(exception).isInstanceOf(NotExistsProductException.class);
        }

        @Test
        @DisplayName("ProductId 에 해당하는 Product 이 현재 판매 중일 경우, ProductModificationPermissionException 을 던진다")
        void throwProductModificationPermissionExceptionWhenProductIsOnSale() {
            // given
            final long onSaleProductId = 1L;
            final Product product = onSaleProduct(onSaleProductId);
            final UpdateProductRequest updateProductRequest = updateProductRequest(product);

            when(productRepository.findById(onSaleProductId)).thenReturn(Optional.of(product));

            // when
            Exception exception = catchException(
                () -> productService.update(onSaleProductId, updateProductRequest));

            // then
            assertThat(exception).isInstanceOf(ProductModificationPermissionException.class);
        }
    }

    @Nested
    @DisplayName("getByProductId 메소드는")
    class GetByProductId_Method {

        @Test
        @DisplayName("id에 해당하는 Product 를 찾을 수 없을경우, NotExistsProductException 을 던진다.")
        void throwNotExistsProductExceptionWhenCannotFindIdMatchedProduct() {
            // given
            final long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(
                Optional.empty());

            // when
            Exception exception = catchException(() -> productService.getByProductId(productId));

            // then
            assertThat(exception).isInstanceOf(NotExistsProductException.class);
        }

        @Test
        @DisplayName("id에 해당하는 Product 가 판매가 끝났을 경우, SaleEndedProductException 을 던진다.")
        void throwSaleEndedProductExceptionWhenCannotFindIdMatchedProduct() {
            // given
            final long productId = 1L;
            final Product saleEndedproduct = afterSaleProduct(productId);

            when(productRepository.findById(productId)).thenReturn(Optional.of(saleEndedproduct));

            // when
            Exception exception = catchException(() -> productService.getByProductId(productId));

            // then
            assertThat(exception).isInstanceOf(SaleEndedProductException.class);
        }

        @Test
        @DisplayName("id에 해당하는 Product 가 존재한다면, ProductResponse 를 반환한다.")
        void ReturnProductResponseWhenExistsIdMatchedProduct() {
            // given
            final long productId = 1L;
            final Product product = beforeSaleProduct(productId);

            final ProductDetailsResponse expected = ProductDtoFixture.fromProduct(product);

            when(productRepository.findById(productId)).thenReturn(
                Optional.of(product));

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
                beforeSaleProductBuilder(1L).build(), beforeSaleProductBuilder(2L).build());

            when(productRepository.findAllWithPagination(page, size))
                .thenReturn(new ProductsPaginationAdminResponse(products, page, totalPage));
            final ProductsAdminResponse expectedProductsAdminResponse = productsResponse(products);

            // when
            final ProductsAdminResponse productsAdminResponse = productService.getProductsInAdmin(page, size);

            // then
            assertThat(productsAdminResponse).usingRecursiveComparison()
                .isEqualTo(expectedProductsAdminResponse);
        }
    }

    @Nested
    @DisplayName("getValidProducts 메서드는")
    class GetValidProducts_Method {

        @Test
        @DisplayName("endTime 이 현재 시간보다 이후인 상품들을 반환한다")
        void returnValidProducts() {
            // given
            final String startTime = "1980-01-01T00:00:00.000Z";
            final long productId = 0L;

            Product product1 = beforeSaleProduct(1L);
            Product product2 = beforeSaleProduct(2L);

            List<Product> products = List.of(product1, product2);

            when(productRepository.findAllValidWithPagination(Instant.parse(startTime), productId, NOW_TIME)).thenReturn(
                new ProductsPaginationResponse(products, false));

            // when
            ProductsResponse result = productService.getValidProducts(startTime, productId);

            // then
            ProductsResponse expected = new ProductsResponse(
                ProductFixture.productsOfProductsResponse(products), false);

            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("getByIdWithAdmin 메소드는")
    class GetByIdWithAdmin_Method {

        @Test
        @DisplayName("productId에 해당하는 Product 를 찾을 수 없을 경우, NotExistsProductException 을 던진다.")
        void throwNotExistsProductExceptionWhenCannotFindIdMatchedProduct() {
            // given
            final long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            // when
            final Exception exception = catchException(
                () -> productService.getByProductIdWithAdmin(productId));

            // then
            assertThat(exception).isInstanceOf(NotExistsProductException.class);
        }

        @Test
        @DisplayName("productId에 해당하는 Product 가 존재한다면, ProductResponse 를 반환한다.")
        void ReturnProductResponseWhenExistsIdMatchedProduct() {
            // given
            final long productId = 1L;
            final Product product = beforeSaleProduct(productId);
            final ProductDetailsResponse expected = ProductDtoFixture.fromProduct(product);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            // when
            final ProductDetailsResponse result = productService.getByProductIdWithAdmin(productId);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
