package shop.woowasap.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woowasap.core.id.api.IdGenerator;
import shop.woowasap.core.util.time.TimeUtil;
import shop.woowasap.order.domain.Order;
import shop.woowasap.order.domain.in.OrderConnector;
import shop.woowasap.order.domain.in.event.PaySuccessEvent;
import shop.woowasap.payment.domain.PayStatus;
import shop.woowasap.payment.domain.Payment;
import shop.woowasap.payment.domain.exception.DoesNotFindOrderException;
import shop.woowasap.payment.domain.exception.PayUserNotMatchException;
import shop.woowasap.payment.domain.in.PaymentUseCase;
import shop.woowasap.payment.domain.in.request.PaymentRequest;
import shop.woowasap.payment.domain.in.response.PaymentResponse;
import shop.woowasap.payment.domain.out.PaymentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        final Payment payment = buildPayment(paymentRequest, order);

        if (Boolean.TRUE.equals(paymentRequest.isSuccess())) {
            paymentRepository.save(payment);
            applicationEventPublisher.publishEvent(
                new PaySuccessEvent(paymentRequest.orderId(), paymentRequest.userId()));
            return PaymentResponse.success();
        }

        paymentRepository.save(payment.changeStatus(PayStatus.FAIL));
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

    private Payment buildPayment(final PaymentRequest paymentRequest, final Order order) {
        return Payment.builder()
            .paymentId(idGenerator.generate())
            .orderId(paymentRequest.orderId())
            .userId(paymentRequest.userId())
            .payStatus(PayStatus.PENDING)
            .payType(paymentRequest.payType())
            .purchasedMoney(order.getTotalPrice())
            .createdAt(timeUtil.now())
            .build();
    }
}
