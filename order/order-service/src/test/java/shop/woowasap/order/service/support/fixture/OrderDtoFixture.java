package shop.woowasap.order.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;

public final class OrderDtoFixture {

    private OrderDtoFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderDtoFixture()\"");
    }

    public static OrdersResponse from(final List<Order> orders, final String locale, final int page,
        final int totalPage, final String fixedName) {

        final List<OrderResponse> orderResponses = getOrderResponse(orders, locale, fixedName);

        return new OrdersResponse(orderResponses, page, totalPage);
    }

    private static List<OrderResponse> getOrderResponse(final List<Order> orders,
        final String locale, final String fixedName) {

        return orders.stream()
            .map(order -> new OrderResponse(order.getId(),
                getOrderProductResponse(order, fixedName),
                order.getTotalPrice().toString(), order.getOrderProducts().size(),
                LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of(locale))))
            .toList();
    }

    private static List<OrderProductResponse> getOrderProductResponse(final Order order,
        final String fixedName) {

        return order.getOrderProducts()
            .stream()
            .map(orderProduct -> new OrderProductResponse(orderProduct.getProductId(), fixedName))
            .toList();
    }
}
