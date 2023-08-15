package shop.woowasap.order.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.in.response.DetailOrderProductResponse;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;

public final class OrderDtoFixture {

    private OrderDtoFixture() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderDtoFixture()\"");
    }

    public static OrdersResponse ordersResponse(final List<Order> orders, final String locale, final int page,
        final int totalPage, final String fixedName, final String fixedPrice) {

        final List<OrderResponse> orderResponses = getOrderResponse(orders, locale, fixedName, fixedPrice);

        return new OrdersResponse(orderResponses, page, totalPage);
    }

    private static List<OrderResponse> getOrderResponse(final List<Order> orders,
        final String locale, final String fixedName, final String fixedPrice) {

        return orders.stream()
            .map(order -> new OrderResponse(order.getId(),
                getOrderProductResponse(order, fixedName, fixedPrice),
                order.getTotalPrice().toString(), LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of(locale))))
            .toList();
    }

    private static List<OrderProductResponse> getOrderProductResponse(final Order order,
        final String fixedName, final String fixedPrice) {

        return order.getOrderProducts()
            .stream()
            .map(orderProduct -> new OrderProductResponse(orderProduct.getProductId(), fixedName, fixedPrice,
                orderProduct.getQuantity()))
            .toList();
    }

    public static DetailOrderResponse detailOrderResponse(final Order order, final String defaultProductName,
        final String defaultProductPrice, final String locale) {

        final List<DetailOrderProductResponse> detailOrderProductResponses = order.getOrderProducts()
            .stream()
            .map(orderProduct -> new DetailOrderProductResponse(orderProduct.getProductId(), defaultProductName,
                defaultProductPrice, orderProduct.getQuantity()))
            .toList();

        return new DetailOrderResponse(order.getId(), detailOrderProductResponses, order.getTotalPrice().toString(),
            LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of(locale)));
    }
}
