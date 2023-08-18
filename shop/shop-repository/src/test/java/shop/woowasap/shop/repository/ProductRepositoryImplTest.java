package shop.woowasap.shop.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import shop.woowasap.BeanScanBaseLocation;
import shop.woowasap.shop.domain.out.response.ProductsPaginationResponse;
import shop.woowasap.shop.domain.product.Product;
import shop.woowasap.shop.repository.support.ProductFixture;

@DataJpaTest
@ContextConfiguration(classes = {BeanScanBaseLocation.class, ProductRepositoryImpl.class})
class ProductRepositoryImplTest {

    @Autowired
    private ProductRepositoryImpl productRepository;

    @Nested
    @DisplayName("persist 메서드는")
    class PersistMethod {

        @Test
        @DisplayName("Product 를 저장한다.")
        void saveProduct() {
            // given
            final long productId = 1L;
            final Product product = ProductFixture.salePriorProduct(productId);

            // when
            final Product result = productRepository.persist(product);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(product);
        }

        @Test
        @DisplayName("Product 를 갱신한다.")
        void updateProduct() {
            // given
            final long productId = 1L;
            final String newName = "newName";
            final Product product = ProductFixture.salePriorProduct(productId);

            // when
            product.update(
                newName,
                product.getDescription().getValue(),
                product.getPrice().getValue().toString(),
                product.getQuantity().getValue(),
                LocalDateTime.ofInstant(product.getStartTime(), ZoneId.of("Asia/Seoul")),
                LocalDateTime.ofInstant(product.getEndTime(), ZoneId.of("Asia/Seoul"))
            );

            final Product result = productRepository.persist(product);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(product);
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class findByIdMethod {

        @Test
        @DisplayName("productId 에 해당하는 Product 를 반환한다.")
        void returnProductWithProductId() {
            // given
            final long productId = 1L;
            final Product product = ProductFixture.salePriorProduct(productId);
            productRepository.persist(product);

            // when
            final Optional<Product> result = productRepository.findById(product.getId());

            // then
            assertThat(result.get()).usingRecursiveComparison().isEqualTo(product);
        }

        @Test
        @DisplayName("productId 에 해당하는 Product 가 없다면, Null 를 반환한다.")
        void returnNullIfNotExistsProductWithProductId() {
            // given
            final long notExistProductId = 1L;

            // when
            final Optional<Product> result = productRepository.findById(notExistProductId);

            // then
            assertThat(result).isEmpty();
        }

    }

    @Nested
    @DisplayName("findAllValidWithPagination 메서드는")
    class findAllValidWithPaginationMethod {

        @Test
        @DisplayName("판매 예정이거나 판매 중인 Product 들을 startTime 기준 오름차순으로 반환한다.")
        void returnAllValidProducts() {
            // given
            final int page = 1;
            final int size = 10;
            final int totalPage = 1;

            final Product salePastProduct = ProductFixture.salePastProduct(1L);
            final Product salePriorProduct = ProductFixture.salePriorProduct(2L);
            final Product onSaleProduct = ProductFixture.onSaleProduct(3L);

            productRepository.persist(salePastProduct);
            productRepository.persist(salePriorProduct);
            productRepository.persist(onSaleProduct);

            ProductsPaginationResponse expected = new ProductsPaginationResponse(
                List.of(onSaleProduct, salePriorProduct),
                page,
                totalPage
            );

            // when
            final ProductsPaginationResponse result = productRepository.findAllValidWithPagination(
                page, size);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("findAllWithPagination 메서드는")
    class findAllWithPaginationMethod {

        @Test
        @DisplayName("모든 Product 들을 startTime 기준 오름차순으로 반환한다.")
        void returnAllProducts() {
            // given
            final int page = 1;
            final int size = 10;
            final int totalPage = 1;

            final Product salePastProduct = ProductFixture.salePastProduct(1L);
            final Product salePriorProduct = ProductFixture.salePriorProduct(2L);
            final Product onSaleProduct = ProductFixture.onSaleProduct(3L);

            productRepository.persist(salePastProduct);
            productRepository.persist(salePriorProduct);
            productRepository.persist(onSaleProduct);

            ProductsPaginationResponse expected = new ProductsPaginationResponse(
                List.of(onSaleProduct, salePastProduct, salePriorProduct),
                page,
                totalPage
            );

            // when
            final ProductsPaginationResponse result = productRepository.findAllWithPagination(
                page, size);

            // then
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("consumeQuantityByProductId 메서드는")
    class consumeQuantityByProductIdMethod {

        @Test
        @DisplayName("product의 quantity를 줄인다.")
        void consumeProductQuantity() {
            // given
            final long productId = 1L;
            final long quantity = 10L;
            final Product persistProduct =
                productRepository.persist(ProductFixture.salePriorProduct(productId, quantity));

            final long expectedQuantity = 0;

            // when
            productRepository.consumeQuantityByProductId(persistProduct.getId(),
                persistProduct.getQuantity().getValue());

            // then
            final Optional<Product> result = productRepository.findById(persistProduct.getId());
            assertThat(result).isPresent();
            assertThat(result.get().getQuantity().getValue()).isEqualTo(expectedQuantity);
        }
    }
}
