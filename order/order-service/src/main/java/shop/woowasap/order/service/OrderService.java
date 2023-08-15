package shop.woowasap.order.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.exception.DoesNotFindCartException;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.in.response.DetailOrderProductResponse;
import shop.woowasap.order.domain.in.response.DetailOrderResponse;
import shop.woowasap.order.domain.in.response.OrderProductResponse;
import shop.woowasap.order.domain.in.response.OrderResponse;
import shop.woowasap.order.domain.in.response.OrdersResponse;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.Payment;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;
import shop.woowasap.order.service.mapper.OrderMapper;
import shop.woowasap.shop.domain.api.cart.CartConnector;
import shop.woowasap.shop.domain.api.product.ProductConnector;
import shop.woowasap.shop.domain.cart.Cart;
import shop.woowasap.shop.domain.product.Product;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService implements OrderUseCase {

    private final Payment payment;
    private final IdGenerator idGenerator;
    private final ProductConnector productConnector;
    private final CartConnector cartConnector;
    private final OrderRepository orderRepository;

    @Value("${shop.woowasap.locale:Asia/Seoul}")
    private String locale;

    @Override
    @Transactional
    public long orderProduct(final OrderProductRequest orderProductRequest) {
        final Product product = getProductByProductId(orderProductRequest.productId());
        final Order order = OrderMapper.toDomain(idGenerator, orderProductRequest, product);

        if (!payment.pay(orderProductRequest.userId())) {
            throw new DoesNotOrderedException();
        }

        orderRepository.persist(order);
        return order.getId();
    }

    @Override
    @Transactional
    public long orderCartByCartIdAndUserId(final long cartId, final long userId) {
        final Cart cart = cartConnector.findByCartIdAndUserId(cartId, userId)
            .orElseThrow(() -> new DoesNotFindCartException(cartId, userId));

        final Order order = OrderMapper.toDomain(idGenerator, userId, cart);

        if (!payment.pay(userId)) {
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

        final List<OrderResponse> orderResponses = orders.stream()
            .map(order -> OrderMapper.toOrderResponse(order, getOrderProductResponse(order), locale))
            .toList();

        return OrderMapper.toOrdersResponse(orderResponses, ordersPaginationResponse.page(),
            ordersPaginationResponse.totalPage());
    }

    private List<OrderProductResponse> getOrderProductResponse(final Order order) {
        return order.getOrderProducts()
            .stream()
            .map(orderProduct -> OrderMapper.toOrderProductResponse(getProductByProductId(orderProduct.getProductId()),
                orderProduct.getQuantity()))
            .toList();
    }

    @Override
    public DetailOrderResponse getOrderByOrderIdAndUserId(final long orderId, final long userId) {
        final Order order = orderRepository.findOrderByOrderIdAndUserId(orderId, userId)
            .orElseThrow(() -> new DoesNotFindOrderException(orderId, userId));

        final List<DetailOrderProductResponse> detailOrderProductResponses = order.getOrderProducts()
            .stream()
            .map(orderProduct -> {
                Product product = getProductByProductId(orderProduct.getProductId());
                return OrderMapper.toDetailOrderProductResponse(orderProduct, product);
            })
            .toList();

        return OrderMapper.toDetailOrderResponse(order, detailOrderProductResponses, locale);
    }

    private Product getProductByProductId(final long productId) {
        return productConnector.findByProductId(productId)
            .orElseThrow(() -> new DoesNotFindProductException(productId));
    }
}