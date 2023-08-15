package shop.woowasap.order.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.OrderProduct;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.Payment;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;
import shop.woowasap.order.service.mapper.OrderMapper;
import shop.woowasap.shop.domain.api.product.ProductConnector;
import shop.woowasap.shop.domain.product.Product;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService implements OrderUseCase {

    private final Payment payment;
    private final IdGenerator idGenerator;
    private final ProductConnector productConnector;
    private final OrderRepository orderRepository;

    @Value("${shop.woowasap.locale:Asia/Seoul}")
    private String locale;

    @Override
    @Transactional
    public long orderProduct(final OrderProductRequest orderProductRequest) {
        final Product product = getProductByProductId(orderProductRequest.productId());
        final Order order = OrderMapper.toDomain(idGenerator, orderProductRequest.userId(),
            product);

        if (!payment.pay(orderProductRequest.userId())) {
            throw new DoesNotOrderedException();
        }

        orderRepository.persist(order);
        return order.getId();
    }

    @Override
    public OrdersResponse getOrderByUserId(final long userId, final int page, final int size) {
        final OrdersPaginationResponse ordersPaginationResponse = orderRepository.findAllOrderByUserId(
            userId, page, size);
        final List<Order> orders = ordersPaginationResponse.orders();

        final List<OrderResponse> orderResponses = getOrderResponse(orders);

        return OrderMapper.toOrdersResponse(orderResponses, ordersPaginationResponse.page(),
            ordersPaginationResponse.totalPage());
    }

    private List<OrderResponse> getOrderResponse(final List<Order> orders) {
        final List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            final List<OrderProductResponse> orderProductResponses = getOrderProductResponse(order);

            final OrderResponse orderResponse = OrderMapper.toOrderResponse(order, orderProductResponses,
                locale);

            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    private List<OrderProductResponse> getOrderProductResponse(final Order order) {
        final List<OrderProductResponse> orderProductResponses = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            final Product product = getProductByProductId(orderProduct.getProductId());
            final OrderProductResponse orderProductResponse = OrderMapper.toOrderProductResponse(
                product);
            orderProductResponses.add(orderProductResponse);
        }
        return orderProductResponses;
    }

    private Product getProductByProductId(final long productId) {
        return productConnector.findByProductId(productId)
            .orElseThrow(() -> new DoesNotFindProductException(productId));
    }
}
