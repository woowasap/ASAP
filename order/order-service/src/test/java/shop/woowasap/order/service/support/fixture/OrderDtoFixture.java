package shop.woowasap.order.service.support.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
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

    private static List<OrderResponse> getOrderResponse(final List<Order> orders, final String locale, final String fixedName) {
        final List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            final List<OrderProductResponse> orderProductResponses = getOrderProductResponse(order, fixedName);

            final OrderResponse orderResponse = new OrderResponse(order.getId(),
                orderProductResponses,
                order.getTotalPrice().toString(), order.getOrderProducts().size(),
                LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of(locale)));

            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    private static List<OrderProductResponse> getOrderProductResponse(final Order order, final String fixedName) {
        final List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            final OrderProductResponse orderProductResponse = new OrderProductResponse(
                orderProduct.getProductId(), fixedName);

            orderProductResponses.add(orderProductResponse);
        }
        return orderProductResponses;
    }
}
