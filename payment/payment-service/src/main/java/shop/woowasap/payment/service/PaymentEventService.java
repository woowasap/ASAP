package shop.woowasap.payment.service;

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
import shop.woowasap.payment.domain.in.PaymentEventConsumer;
import shop.woowasap.payment.domain.out.PaymentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentEventService implements PaymentEventConsumer {

    private final PaymentRepository paymentRepository;

    @Override
    @Async
    @Transactional
    @EventListener(StockSuccessEvent.class)
    public void successPayment(final StockSuccessEvent stockSuccessEvent) {
        final Payment payment = paymentRepository.findByOrderId(stockSuccessEvent.orderId())
            .orElseThrow(() -> new DoesNotFindPaymentException(stockSuccessEvent.orderId()));
        paymentRepository.save(payment.changeStatus(PayStatus.SUCCESS));
    }

    @Override
    @Async
    @Transactional
    @EventListener(StockFailEvent.class)
    public void cancelPayment(final StockFailEvent stockFailEvent) {
        final Payment payment = paymentRepository.findByOrderId(stockFailEvent.orderId())
            .orElseThrow(() -> new DoesNotFindPaymentException(stockFailEvent.orderId()));
        paymentRepository.save(payment.changeStatus(PayStatus.CANCELD));
    }
}
