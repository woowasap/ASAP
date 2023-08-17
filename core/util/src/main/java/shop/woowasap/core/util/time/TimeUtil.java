package shop.woowasap.core.util.time;

import java.time.Clock;
import java.time.Instant;

public interface TimeUtil {

	Instant now();

	void clock(Clock clock);
}
