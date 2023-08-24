package shop.woowasap.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.order.domain.out.event.OrderCanceledEvent;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.exception.DoesNotFindPaymentException;
import shop.woowasap.payment.domain.out.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentEventService {

    private final PaymentRepository paymentRepository;

    @Transactional
    @EventListener(OrderCanceledEvent.class)
    public void cancelPay(final OrderCanceledEvent orderCanceledEvent) {
        final Payment payment = paymentRepository.findByOrderId(orderCanceledEvent.orderId())
            .orElseThrow(() -> new DoesNotFindPaymentException(orderCanceledEvent.orderId()));

        payment.changeStatus(PayStatus.CANCELED);

        paymentRepository.persist(payment);
    }
}
