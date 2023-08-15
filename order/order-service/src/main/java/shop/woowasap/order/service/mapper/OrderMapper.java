package shop.woowasap.order.service.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.in.response.DetailOrderProductResponse;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.shop.domain.product.Product;

public final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("Cannot invoke constructor \"OrderMapper()\"");
    }

    public static Order toDomain(final IdGenerator idGenerator, final long userId,
        final Product product) {
        return Order.builder()
            .id(idGenerator.generate())
            .userId(userId)
            .orderProducts(List.of(OrderProduct
                .builder()
                .productId(product.getId())
                .price(product.getPrice().getValue().toString())
                .quantity(product.getQuantity().getValue())
                .startTime(product.getStartTime())
                .endTime(product.getEndTime())
                .build()))
            .build();
    }

    public static OrderProductResponse toOrderProductResponse(final Product product, final long quantity) {
        return new OrderProductResponse(product.getId(), product.getName().getValue(),
            product.getPrice().getValue().toString(), quantity);
    }

    public static OrderResponse toOrderResponse(final Order order,
        final List<OrderProductResponse> orderProductResponses, final String locale) {

        return new OrderResponse(order.getId(), orderProductResponses,
            order.getTotalPrice().toString(), LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of(locale)));
    }

    public static OrdersResponse toOrdersResponse(final List<OrderResponse> orderResponses,
        final int page, final int totalPage) {

        return new OrdersResponse(orderResponses, page, totalPage);
    }

    public static DetailOrderResponse toDetailOrderResponse(final Order order,
        final List<DetailOrderProductResponse> detailOrderProductResponses, final String locale) {

        return new DetailOrderResponse(order.getId(), detailOrderProductResponses, order.getTotalPrice().toString(),
            LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of(locale)));
    }

    public static DetailOrderProductResponse toDetailOrderProductResponse(final OrderProduct orderProduct,
        final Product product) {
        return new DetailOrderProductResponse(orderProduct.getProductId(), product.getName().getValue(),
            product.getPrice().getValue().toString(), orderProduct.getQuantity());
    }
}
