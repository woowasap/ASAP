package shop.woowasap.payment.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.out.PaymentRepository;
import shop.woowasap.payment.repository.entity.PaymentEntity;
import shop.woowasap.payment.repository.jpa.PaymentEntityRepository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentEntityRepository paymentEntityRepository;

    @Override
    public Payment save(final Payment payment) {
        final PaymentEntity paymentEntity = PaymentEntityMapper.toEntity(payment);
        final PaymentEntity result = paymentEntityRepository.save(paymentEntity);
        return PaymentEntityMapper.toDomain(result);
    }

    @Override
    public Optional<Payment> findByOrderId(final long orderId) {
        return paymentEntityRepository.findByOrderId(orderId).map(PaymentEntityMapper::toDomain);
    }

    @Override
    public List<Payment> findAllByOrderId(final long orderId) {
        return paymentEntityRepository.findByOrderIdOrderByUpdatedAtDesc(orderId).stream()
            .map(PaymentEntityMapper::toDomain)
            .toList();
    }
}
