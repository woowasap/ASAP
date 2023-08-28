package shop.woowasap.accept;

import static shop.woowasap.accept.support.api.ShopApiSupporter.registerProduct;
import static shop.woowasap.accept.support.fixture.ProductFixture.registerValidProductRequest;
import static shop.woowasap.accept.support.fixture.ProductFixture.updateProductRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertBadRequest;
import static shop.woowasap.accept.support.valid.HttpValidator.assertOk;
import static shop.woowasap.accept.support.valid.ShopValidator.assertProductRegistered;
import static shop.woowasap.accept.support.valid.ShopValidator.assertAdminProductsFound;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.woowasap.accept.support.api.AuthApiSupporter;
import shop.woowasap.accept.support.api.ShopApiSupporter;
import shop.woowasap.accept.support.fixture.ProductFixture;
import shop.woowasap.accept.support.valid.HttpValidator;
import shop.woowasap.accept.support.valid.ShopValidator;
import shop.woowasap.shop.domain.in.product.request.RegisterProductRequest;
import shop.woowasap.shop.domain.in.product.request.UpdateProductRequest;
import shop.woowasap.shop.domain.in.product.response.ProductDetailsResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsAdminResponse;
import shop.woowasap.shop.domain.in.product.response.ProductsResponse;

@DisplayName("Product 인수테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setAccessToken() {
        accessToken = AuthApiSupporter.adminAccessToken();
    }

    @Test
    @DisplayName("상품 내용을 수정한다.")
    void updateProduct() {
        // given
        final RegisterProductRequest registerProductRequest = registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest);

        final long productId = Long.parseLong(registerResponse
            .header("Location")
            .split("/")[4]);

        final UpdateProductRequest updateProductRequest = updateProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(
            accessToken,
            productId,
            updateProductRequest);

        // then
        assertOk(response);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 수정하려고 하는경우 BAD REQUEST 응답을 받는다.")
    void updateProductWithNotFound() {
        // given
        final long invalidProductId = 123;
        final UpdateProductRequest updateProductRequest = updateProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter.updateProduct(accessToken,
            invalidProductId,
            updateProductRequest);

        // then
        assertBadRequest(response);
    }

    @Test
    @DisplayName("POST /products 요청을 통해서 상품을 생성한다.")
    void createProduct() {
        // given
        final RegisterProductRequest registerProductRequest = registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .registerProduct(accessToken, registerProductRequest);

        // then
        assertProductRegistered(response);
    }

    @Test
    @DisplayName("기한이 유효한 상품의 목록을 조회한다.")
    void findValidProducts() {
        // given
        final RegisterProductRequest invalidRegisterProductRequest = ProductFixture.registerInvalidProductRequest();
        final RegisterProductRequest validRegisterProductRequest = registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        registerProduct(accessToken, invalidRegisterProductRequest);
        registerProduct(accessToken, validRegisterProductRequest);
        registerProduct(accessToken, validRegisterProductRequest);

        final List<RegisterProductRequest> registerProductRequests = List.of(
            validRegisterProductRequest, validRegisterProductRequest);

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter.getAllProducts();
        final ProductsResponse expected = ProductFixture.productsResponse(registerProductRequests);

        // then
        ShopValidator.assertProductsFound(response, expected);
    }

    @Test
    @DisplayName("GET /v1/admin/products 요청을 통해서 등록 되어 있는 전체 상품을 조회할 수 있다.")
    void getAdminProducts() {
        // given
        final RegisterProductRequest registerProductRequest = registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));
        final ExtractableResponse<Response> registerResponse = registerProduct(accessToken,
            registerProductRequest);
        final long productId = Long.parseLong(registerResponse.header("Location").split("/")[4]);

        // when
        final ExtractableResponse<Response> response = ShopApiSupporter
            .getRegisteredProducts(accessToken);

        // then
        assertAdminProductsFound(response, ProductFixture.productsAdminResponse(productId));
    }

    @Test
    @DisplayName("저장되어있는 특정 상품을 조회할경우, 상품의 정보가 응답된다.")
    void findSpecificProduct() {
        // given
        final RegisterProductRequest registerProductRequest = registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        ShopApiSupporter.registerProduct(accessToken, registerProductRequest);

        final ProductsResponse productsResponse = ShopApiSupporter.getAllProducts()
            .as(ProductsResponse.class);

        final long anyProductId = productsResponse.products().get(0).productId();

        final ProductDetailsResponse expected = ProductFixture.productResponse(anyProductId,
            registerProductRequest);

        // when
        final ExtractableResponse<Response> result = ShopApiSupporter.getProduct(anyProductId);

        // then
        ShopValidator.assertProduct(result, expected);
    }

    @Test
    @DisplayName("productId에 해당하는 상품을 찾을 수 없다면, 400 BadRequest가 응답된다.")
    void returnBadRequestWhenCannotFoundProduct() {
        // given
        final long notFoundProductId = 123L;

        // when
        final ExtractableResponse<Response> result = ShopApiSupporter.getProduct(notFoundProductId);

        // then
        HttpValidator.assertBadRequest(result);
    }

    @Test
    @DisplayName("Admin 유저가 저장되어있는 특정 상품을 조회할경우, 상품의 정보가 응답된다.")
    void findSpecificProductWithAdmin() {
        // given

        final RegisterProductRequest registerProductRequest = registerValidProductRequest(
            Instant.now().plus(20, ChronoUnit.MINUTES),
            Instant.now().plus(60 * 6, ChronoUnit.MINUTES));

        ShopApiSupporter.registerProduct(accessToken, registerProductRequest);

        final ProductsAdminResponse productsAdminResponse = ShopApiSupporter.getRegisteredProducts(
                accessToken)
            .as(ProductsAdminResponse.class);

        final long anyProductId = productsAdminResponse.products().get(0).productId();

        final ProductDetailsResponse expected = ProductFixture.productResponse(anyProductId,
            registerProductRequest);

        // when
        final ExtractableResponse<Response> result = ShopApiSupporter.getProductWithAdmin(
            accessToken,
            anyProductId);

        // then
        ShopValidator.assertProduct(result, expected);
    }

    @Test
    @DisplayName("Admin 유저가 productId에 해당하는 상품을 찾을 수 없다면, 400 BadRequest가 응답된다.")
    void returnBadRequestWhenCannotFoundProductWithAdmin() {
        // given
        final long notFoundProductId = 123L;

        // when
        final ExtractableResponse<Response> result = ShopApiSupporter.getProductWithAdmin(
            accessToken,
            notFoundProductId);

        // then
        HttpValidator.assertBadRequest(result);
    }
}
