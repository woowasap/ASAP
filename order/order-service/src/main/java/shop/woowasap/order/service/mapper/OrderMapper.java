package shop.woowasap.order.service.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.DetailOrderProductResponse;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.product.Product;

public final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderMapper()\"");
    }

    public static Order toDomain(final IdGenerator idGenerator,
        final OrderProductRequest orderProductRequest, final Product product,
        final Instant createdAt) {
        return Order.builder()
            .id(idGenerator.generate())
            .userId(orderProductRequest.userId())
            .orderProducts(
                List.of(toOrderProduct(product, orderProductRequest.quantity(), createdAt)))
            .createdAt(createdAt)
            .build();
    }

    public static Order toDomain(final IdGenerator idGenerator, final long userId,
        final Cart cart, final Instant createdAt) {
        final List<OrderProduct> orderProducts = cart.getCartProducts()
            .stream()
            .map(cartProduct -> toOrderProduct(cartProduct.getProduct(),
                cartProduct.getQuantity().getValue(), createdAt))
            .toList();

        return Order.builder()
            .id(idGenerator.generate())
            .userId(userId)
            .orderProducts(orderProducts)
            .createdAt(createdAt)
            .build();
    }

    private static OrderProduct toOrderProduct(final Product product, final long quantity,
        final Instant nowTime) {
        return OrderProduct.builder()
            .productId(product.getId())
            .price(product.getPrice().getValue().multiply(BigInteger.valueOf(quantity)).toString())
            .name(product.getName().getValue())
            .quantity(quantity)
            .startTime(product.getSaleTime().getStartTime())
            .nowTime(nowTime)
            .endTime(product.getSaleTime().getEndTime())
            .build();
    }

    public static OrderProductResponse toOrderProductResponse(final OrderProduct orderProduct) {
        return new OrderProductResponse(orderProduct.getProductId(), orderProduct.getName(),
            orderProduct.getPrice().divide(BigInteger.valueOf(orderProduct.getQuantity()))
                .toString()
            , orderProduct.getQuantity());
    }

    public static OrderResponse toOrderResponse(final Order order,
        final List<OrderProductResponse> orderProductResponses) {

        return new OrderResponse(order.getId(), orderProductResponses,
            order.getTotalPrice().toString(), order.getOrderType().toString(),
            LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of("UTC")));
    }

    public static OrdersResponse toOrdersResponse(final List<OrderResponse> orderResponses,
        final int page, final int totalPage) {

        return new OrdersResponse(orderResponses, page, totalPage);
    }

    public static DetailOrderResponse toDetailOrderResponse(final Order order,
        final List<DetailOrderProductResponse> detailOrderProductResponses) {

        return new DetailOrderResponse(order.getId(), detailOrderProductResponses,
            order.getTotalPrice().toString(), order.getOrderType().toString(),
            LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of("UTC")));
    }

    public static DetailOrderProductResponse toDetailOrderProductResponse(
        final OrderProduct orderProduct) {
        return new DetailOrderProductResponse(orderProduct.getProductId(),
            orderProduct.getName(),
            orderProduct.getPrice().divide(BigInteger.valueOf(orderProduct.getQuantity()))
                .toString(),
            orderProduct.getQuantity());
    }
}
