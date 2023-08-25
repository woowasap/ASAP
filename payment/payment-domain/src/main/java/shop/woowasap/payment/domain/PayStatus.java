package shop.woowasap.payment.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayStatus {

    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    CANCELED("CANCELED"),
    ;

    private final String value;
}
