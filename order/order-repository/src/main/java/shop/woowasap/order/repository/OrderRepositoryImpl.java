package shop.woowasap.order.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.out.OrderRepository;
import shop.woowasap.order.domain.out.response.OrdersPaginationResponse;
import shop.woowasap.order.repository.entity.OrderEntity;
import shop.woowasap.order.repository.jpa.OrderJpaRepository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public void persist(final Order order) {
        orderJpaRepository.save(new OrderEntity(order));
    }

    @Override
    public OrdersPaginationResponse findAllOrderByUserId(final long userId, final int page,
        final int size) {
        final PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").descending());
        final Page<OrderEntity> pagination = orderJpaRepository.findAllByUserId(userId, pageRequest);

        final List<Order> orders = pagination.get()
            .map(OrderEntity::toDomain)
            .toList();

        return new OrdersPaginationResponse(orders, page, pagination.getTotalPages());
    }

    @Override
    public Optional<Order> findOrderByOrderIdAndUserId(final long orderId, final long userId) {
        final Optional<OrderEntity> orderEntity = orderJpaRepository.findByIdAndUserId(orderId, userId);
        return orderEntity.map(OrderEntity::toDomain);
    }
}
