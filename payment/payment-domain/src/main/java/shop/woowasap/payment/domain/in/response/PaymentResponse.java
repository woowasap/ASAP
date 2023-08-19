package shop.woowasap.payment.domain.in.response;

public record PaymentResponse(
    Boolean isSuccess
) {

    public static PaymentResponse success() {
        return new PaymentResponse(true);
    }

    public static PaymentResponse fail() {
        return new PaymentResponse(false);
    }
}
