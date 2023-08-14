package shop.woowasap.order.payment.random;

import java.util.Random;
import org.springframework.stereotype.Service;
import shop.woowasap.order.domain.out.Payment;

@Service
public class RandomOrderPayment implements Payment {

    private static final int MAX_SLEEP_BOUND = 1001;
    private static final Random random = new Random();

    @Override
    public boolean pay(long userId) {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_BOUND));
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
        return true;
    }
}
