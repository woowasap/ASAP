package shop.woowasap.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.core.util.time.TimeUtil;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.order.domain.in.OrderConnector;
import shop.woowasap.order.domain.in.event.PaySuccessEvent;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.exception.PayUserNotMatchException;
import shop.woowasap.payment.domain.in.PaymentUseCase;
import shop.woowasap.payment.domain.in.request.PaymentRequest;
import shop.woowasap.payment.domain.in.response.PaymentResponse;
import shop.woowasap.payment.domain.out.PaymentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PaymentRepository paymentRepository;
    private final OrderConnector orderConnector;
    private final IdGenerator idGenerator;
    private final TimeUtil timeUtil;

    @Override
    @Transactional
    public PaymentResponse pay(final PaymentRequest paymentRequest) {
        final Order order = findAndValidateOrder(paymentRequest);

        savePayment(paymentRequest, order);

        if (Boolean.TRUE.equals(paymentRequest.isSuccess())) {
            applicationEventPublisher.publishEvent(
                new PaySuccessEvent(paymentRequest.orderId(), paymentRequest.userId()));
            return PaymentResponse.success();
        }
        return PaymentResponse.fail();
    }

    private Order findAndValidateOrder(final PaymentRequest paymentRequest) {
        final Order order = orderConnector.findByOrderId(paymentRequest.orderId())
            .orElseThrow(() -> new DoesNotFindOrderException(paymentRequest.orderId(),
                paymentRequest.userId()));

        if (!paymentRequest.userId().equals(order.getUserId())) {
            throw new PayUserNotMatchException();
        }
        return order;
    }

    private void savePayment(final PaymentRequest paymentRequest, final Order order) {
        final Payment payment = Payment.builder()
            .paymentId(idGenerator.generate())
            .orderId(paymentRequest.orderId())
            .userId(paymentRequest.userId())
            .payStatus(PayStatus.PENDING)
            .payType(paymentRequest.payType())
            .purchasedMoney(order.getTotalPrice())
            .createdAt(timeUtil.now())
            .build();

        paymentRepository.save(payment);
    }
}
