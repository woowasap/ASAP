package shop.woowasap.core.util.time;

import java.time.Clock;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class InstantClock implements TimeUtil {

    private Clock clock = Clock.systemUTC();

    @Override
    public Instant now() {
        return Instant.now(clock);
    }

    @Override
    public void clock(Clock clock) {
        this.clock = clock;
    }
}
