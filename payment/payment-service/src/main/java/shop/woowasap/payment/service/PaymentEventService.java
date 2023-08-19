package shop.woowasap.payment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.order.domain.out.event.StockFailEvent;
import shop.woowasap.order.domain.out.event.StockSuccessEvent;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.exception.DoesNotFindPaymentException;
import shop.woowasap.payment.domain.out.PaymentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentEventService {

    private final PaymentRepository paymentRepository;

    @Async
    @Transactional
    @EventListener(StockSuccessEvent.class)
    public void successPayment(final StockSuccessEvent stockSuccessEvent) {
        final List<Payment> payments = paymentRepository.findAllByOrderId(
            stockSuccessEvent.orderId());
        if (payments.isEmpty()) {
            throw new DoesNotFindPaymentException(stockSuccessEvent.orderId());
        }
        final Payment payment = payments.get(0);
        paymentRepository.save(payment.changeStatus(PayStatus.SUCCESS));
    }

    @Async
    @Transactional
    @EventListener(StockFailEvent.class)
    public void cancelPayment(final StockFailEvent stockFailEvent) {
        final List<Payment> payments = paymentRepository.findAllByOrderId(
            stockFailEvent.orderId());
        if (payments.isEmpty()) {
            throw new DoesNotFindPaymentException(stockFailEvent.orderId());
        }
        final Payment payment = payments.get(0);
        paymentRepository.save(payment.changeStatus(PayStatus.CANCELED));
    }
}
