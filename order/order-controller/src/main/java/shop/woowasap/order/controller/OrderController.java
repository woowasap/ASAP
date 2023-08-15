package shop.woowasap.order.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.exception.InvalidOrderProductException;
import shop.woowasap.order.domain.exception.InvalidPriceException;
import shop.woowasap.order.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.order.domain.exception.InvalidQuantityException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.OrdersResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private static final long MOCK_USER_ID = 1L;

    private final OrderUseCase orderUseCase;

    @PostMapping("/products/{product-id}")
    public ResponseEntity<Void> orderProduct(@PathVariable("product-id") final long productId,
        @RequestBody final OrderProductQuantityRequest orderProductQuantityRequest) {

        final OrderProductRequest orderProductRequest = new OrderProductRequest(MOCK_USER_ID,
            productId,
            orderProductQuantityRequest.quantity());
        final long orderId = orderUseCase.orderProduct(orderProductRequest);

        return ResponseEntity.created(URI.create("/v1/orders/" + orderId))
            .build();
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> getOrderByUserId(
        @RequestParam(defaultValue = "1") final int page,
        @RequestParam(defaultValue = "20") final int size) {

        return ResponseEntity.ok(orderUseCase.getOrderByUserId(MOCK_USER_ID, page, size));
    }

    @ExceptionHandler({DoesNotFindProductException.class,
        DoesNotOrderedException.class,
        InvalidOrderProductException.class,
        InvalidPriceException.class,
        InvalidProductSaleTimeException.class,
        InvalidQuantityException.class})
    private ResponseEntity<Void> handleBadRequest(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().build();
    }
}
