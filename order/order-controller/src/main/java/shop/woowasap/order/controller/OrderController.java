package shop.woowasap.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.auth.domain.in.LoginUser;
import shop.woowasap.core.util.web.ErrorTemplate;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;
import shop.woowasap.order.domain.exception.DoesNotFindCartException;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.exception.InvalidOrderProductException;
import shop.woowasap.order.domain.exception.InvalidPriceException;
import shop.woowasap.order.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.order.domain.exception.InvalidQuantityException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderIdResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping("/products/{product-id}")
    public ResponseEntity<OrderIdResponse> orderProduct(
        @PathVariable("product-id") final long productId,
        @RequestBody final OrderProductQuantityRequest orderProductQuantityRequest,
        @LoginUser final Long userId) {

        final OrderProductRequest orderProductRequest = new OrderProductRequest(userId,
            productId,
            orderProductQuantityRequest.quantity());

        final OrderIdResponse orderIdResponse = orderUseCase.orderProduct(orderProductRequest);

        return ResponseEntity.ok(orderIdResponse);
    }

    @PostMapping("/carts/{cart-id}")
    public ResponseEntity<OrderIdResponse> orderCart(@PathVariable("cart-id") final long cartId,
        @LoginUser final Long userId) {

        final OrderIdResponse orderIdResponse = orderUseCase.orderCartByCartIdAndUserId(cartId,
            userId);

        return ResponseEntity.ok(orderIdResponse);
    }

    @GetMapping
    public ResponseEntity<OrdersResponse> getOrdersByUserId(
        @RequestParam(defaultValue = "1") final int page,
        @RequestParam(defaultValue = "20") final int size,
        @LoginUser final Long userId) {

        return ResponseEntity.ok(orderUseCase.getOrderByUserId(userId, page, size));
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<DetailOrderResponse> getOrderByOrderIdAndUserID(
        @PathVariable("order-id") final long orderId, @LoginUser final Long userId) {

        return ResponseEntity.ok(orderUseCase.getOrderByOrderIdAndUserId(orderId, userId));
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("order-id") final long orderId,
        @LoginUser final Long userId) {

        orderUseCase.cancelOrder(orderId, userId);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({DoesNotFindProductException.class,
        DoesNotOrderedException.class,
        InvalidOrderProductException.class,
        InvalidPriceException.class,
        InvalidProductSaleTimeException.class,
        InvalidQuantityException.class,
        DoesNotFindOrderException.class,
        DoesNotFindCartException.class})
    private ResponseEntity<ErrorTemplate> handleBadRequest(final Exception exception) {
        log.info(exception.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(exception.getMessage()));
    }
}
