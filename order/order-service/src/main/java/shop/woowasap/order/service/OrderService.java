package shop.woowasap.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.exception.DoesNotFindProductException;
import shop.woowasap.order.domain.exception.DoesNotOrderedException;
import shop.woowasap.order.domain.in.OrderUseCase;
import shop.woowasap.order.domain.in.request.OrderProductRequest;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.Payment;
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

    @Override
    @Transactional
    public long orderProduct(final OrderProductRequest orderProductRequest) {
        final Product product = productConnector.findByProductId(orderProductRequest.productId())
            .orElseThrow(() -> new DoesNotFindProductException(orderProductRequest.productId()));
        final Order order = OrderMapper.toDomain(idGenerator, orderProductRequest.userId(), product);

        if (!payment.pay(orderProductRequest.userId())) {
            throw new DoesNotOrderedException();
        }

        orderRepository.persist(order);
        return order.getId();
    }
}
