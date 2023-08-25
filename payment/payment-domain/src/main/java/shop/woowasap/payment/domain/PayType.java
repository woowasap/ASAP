package shop.woowasap.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayType {
    CARD("CARD"),
    DEPOSIT("DEPOSIT"),
    ;

    private final String value;
}
