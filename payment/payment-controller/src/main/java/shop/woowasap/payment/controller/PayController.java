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
import shop.woowasap.order.domain.exception.DoesNotFindOrderException;
import shop.woowasap.payment.controller.request.PayRequest;
import shop.woowasap.payment.domain.PayType;
import shop.woowasap.payment.domain.exception.DoesNotFindPaymentException;
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

    @ExceptionHandler(value = {MethodArgumentNotValidException.class,
        DoesNotFindPaymentException.class,
        DoesNotFindOrderException.class,
        IllegalArgumentException.class})
    public ResponseEntity<String> handleBadRequest(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = PayUserNotMatchException.class)
    public ResponseEntity<String> handleForbidden(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception e) {
        log.error("예상치 못한 예외 발생", e);
        return ResponseEntity.internalServerError().body("예상치 못한 에러가 발생했습니다.");
    }
}
