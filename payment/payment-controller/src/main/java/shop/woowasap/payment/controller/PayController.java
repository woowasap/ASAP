package shop.woowasap.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.woowasap.auth.domain.in.LoginUser;
import shop.woowasap.core.util.web.ErrorTemplate;
import shop.woowasap.payment.controller.request.PayRequest;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.exception.DoesNotFindOrderException;
import shop.woowasap.payment.domain.exception.DoesNotFindPaymentException;
import shop.woowasap.payment.domain.exception.DuplicatedPayException;
import shop.woowasap.payment.domain.exception.PayUserNotMatchException;
import shop.woowasap.payment.domain.in.PaymentUseCase;
import shop.woowasap.payment.domain.in.request.PaymentRequest;
import shop.woowasap.payment.domain.in.response.PaymentResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pays")
public class PayController {

    private final PaymentUseCase paymentUseCase;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> pay(@RequestBody @Valid final PayRequest payRequest,
        @PathVariable("orderId") final Long orderId,
        @LoginUser final Long userId) {
        final PaymentResponse response = paymentUseCase.pay(
            new PaymentRequest(orderId, userId, PayType.valueOf(payRequest.payType()),
                payRequest.isSuccess()));
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(value = {
        DoesNotFindPaymentException.class,
        DoesNotFindOrderException.class,
        DuplicatedPayException.class,
        MethodArgumentNotValidException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<ErrorTemplate> handleBadRequest(final Exception exception) {
        log.info(exception.getMessage());

        return ResponseEntity.badRequest()
            .body(ErrorTemplate.of(exception.getMessage()));
    }

    @ExceptionHandler(value = PayUserNotMatchException.class)
    public ResponseEntity<ErrorTemplate> handleForbidden(final Exception exception) {
        log.info(exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ErrorTemplate.of(exception.getMessage()));
    }
}
