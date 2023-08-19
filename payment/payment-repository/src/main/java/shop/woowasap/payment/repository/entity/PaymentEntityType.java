package shop.woowasap.payment.repository.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentEntityType {
    CARD("CARD"),
    DEPOSIT("DEPOSIT"),
    ;

    private final String value;
}
