package shop.woowasap.shop.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import shop.woowasap.shop.domain.exception.InvalidProductSaleDurationException;
import shop.woowasap.shop.domain.exception.InvalidProductSaleTimeException;
import shop.woowasap.shop.domain.exception.InvalidProductStartTimeException;

@DisplayName("Sale Time 테스트")
public class SaleTimeTest {

    @Nested
    @DisplayName("SaleTime Builder 는")
    class SaleTime_Constructor {

        @Test
        @DisplayName("유효한 startTime 과 endTime 이라면 SaleTime 을 생성한다.")
        void createSaleTime() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:00:00.000Z");
            final Instant endTime = Instant.parse("2023-08-05T21:00:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T20:00:00.000Z");

            // when
            final Exception exception = Assertions.catchException(() -> SaleTime.builder()
                .startTime(startTime)
                .endTime(endTime)
                .nowTime(nowTime)
                .build());

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("startTime 이 endTime 보다 같거나 크다면 InvalidProductSaleTimeException 를 던진다")
        void throwExceptionWhenStartTimeIsGreaterThanEndTime() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:00:00.000Z");
            final Instant endTime = Instant.parse("2023-08-05T19:00:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T20:00:00.000Z");

            // when
            final Exception exception = Assertions.catchException(() -> SaleTime.builder()
                .startTime(startTime)
                .endTime(endTime)
                .nowTime(nowTime)
                .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleTimeException.class);
        }

        @Test
        @DisplayName("startTime 이 현재 시간으로부터 10분 이전이라면 InvalidProductStartTimeException 를 던진다.")
        void throwExceptionWhenStartIn10MinuteFromNow() {
            // given
            final Instant startTime = Instant.parse("2023-08-01T20:00:00.000Z");
            final Instant endTime = Instant.parse("2023-08-05T19:00:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T20:00:00.000Z");

            // when
            final Exception exception = Assertions.catchException(() -> SaleTime.builder()
                .startTime(startTime)
                .endTime(endTime)
                .nowTime(nowTime)
                .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductStartTimeException.class);
        }

        @Test
        @DisplayName("endTime - startTime 이 1시간 미만이라면 InvalidProductSaleDurationException 를 던진다.")
        void throwExceptionSaleTimeIsUnder1Hour() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:00:00.000Z");
            final Instant endTime = Instant.parse("2023-08-05T20:30:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T20:00:00.000Z");

            // when
            final Exception exception = Assertions.catchException(() -> SaleTime.builder()
                .startTime(startTime)
                .endTime(endTime)
                .nowTime(nowTime)
                .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleDurationException.class);
        }

        @Test
        @DisplayName("endTime - startTime 이 10일을 초과한다면 InvalidProductSaleDurationException 를 던진다.")
        void throwExceptionSaleTimeIsOver12Hour() {
            // given
            final Instant startTime = Instant.parse("2023-08-05T20:00:00.000Z");
            final Instant endTime = Instant.parse("2023-08-20T20:00:00.000Z");
            final Instant nowTime = Instant.parse("2023-08-01T20:00:00.000Z");

            // when
            final Exception exception = Assertions.catchException(() -> SaleTime.builder()
                .startTime(startTime)
                .endTime(endTime)
                .nowTime(nowTime)
                .build());

            // then
            assertThat(exception).isInstanceOf(InvalidProductSaleDurationException.class);
        }
    }

    @Nested
    @DisplayName("isOnSale 메서드는")
    class isOnSale_Method {

        @Test
        @DisplayName("nowTime 이 StartTime 과 EndTime 사이라면 True 를 반환한다.")
        void returnTrueWhenNowTimeIsBetweenStartTimeAndEndTime() {
            // given
            final SaleTime saleTime = SaleTime.builder()
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T21:00:00.000Z"))
                .nowTime(Instant.parse("2023-08-01T20:00:00.000Z"))
                .build();

            final Instant nowTime = Instant.parse("2023-08-05T20:30:00.000Z");

            // when
            final boolean result = saleTime.isOnSale(nowTime);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("nowTime 이 StartTime 보다 작다면 False 를 반환한다.")
        void returnFalseWhenNowTimeIsLessThanStartTime() {
            // given
            final SaleTime saleTime = SaleTime.builder()
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T21:00:00.000Z"))
                .nowTime(Instant.parse("2023-08-01T20:00:00.000Z"))
                .build();

            final Instant nowTime = Instant.parse("2023-08-04T20:30:00.000Z");

            // when
            final boolean result = saleTime.isOnSale(nowTime);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("nowTime 이 EndTime 보다 크다면 False 를 반환한다.")
        void returnFalseWhenNowTimeIsGreaterThanEndTime() {
            // given
            final SaleTime saleTime = SaleTime.builder()
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T21:00:00.000Z"))
                .nowTime(Instant.parse("2023-08-01T20:00:00.000Z"))
                .build();

            final Instant nowTime = Instant.parse("2023-08-09T20:30:00.000Z");

            // when
            final boolean result = saleTime.isOnSale(nowTime);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("isEndTimeBefore 메서드는")
    class IsEndTimeBefore_Method {

        @Test
        @DisplayName("time 이 EndTime 보다 크다면 True 를 반환한다.")
        void returnFalseWhenTimeIsGreaterThanEndTime() {
            // given
            final SaleTime saleTime = SaleTime.builder()
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T21:00:00.000Z"))
                .nowTime(Instant.parse("2023-08-01T20:00:00.000Z"))
                .build();

            final Instant time = Instant.parse("2023-08-05T21:30:00.000Z");

            // when
            final boolean result = saleTime.isEndTimeBefore(time);
            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("time 이 EndTime 보다 작다면 False 를 반환한다.")
        void returnFalseWhenTimeIsLessThanEndTime() {
            // given
            final SaleTime saleTime = SaleTime.builder()
                .startTime(Instant.parse("2023-08-05T20:00:00.000Z"))
                .endTime(Instant.parse("2023-08-05T21:00:00.000Z"))
                .nowTime(Instant.parse("2023-08-01T20:00:00.000Z"))
                .build();

            final Instant time = Instant.parse("2023-08-04T20:30:00.000Z");

            // when
            final boolean result = saleTime.isEndTimeBefore(time);
            // then
            assertThat(result).isFalse();
        }
    }

}
