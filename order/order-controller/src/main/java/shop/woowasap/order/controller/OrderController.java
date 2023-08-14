package shop.woowasap.order.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.order.controller.request.OrderProductQuantityRequest;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private static final long MOCK_USER_ID = 1L;

    private final OrderUseCase orderUseCase;

    @PostMapping("/products/{product-id}")
    public ResponseEntity<Void> orderProduct(@PathVariable("product-id") long productId,
        @RequestBody OrderProductQuantityRequest orderProductQuantityRequest) {

        OrderProductRequest orderProductRequest = new OrderProductRequest(MOCK_USER_ID, productId,
            orderProductQuantityRequest.quantity());
        long orderId = orderUseCase.orderProduct(orderProductRequest);

        return ResponseEntity.created(URI.create("/v1/orders/" + orderId))
            .build();
    }

}
