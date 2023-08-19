package shop.woowasap.payment.repository;

import java.util.List;
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
    public List<Payment> findAllByOrderId(final long orderId) {
        final List<PaymentEntity> result = paymentEntityRepository.findByOrderIdOrderByUpdatedAtDesc(
            orderId);
        return result.stream().map(PaymentEntityMapper::toDomain).toList();
    }
}
