package shop.woowasap.payment.repository.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentEntityStatus {

    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    CANCELED("CANCELED"),
    ;

    private final String value;
}
