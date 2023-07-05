package comn.kingname.hangangview.scheduler;

import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeScheduler {

        private final TradeService tradeService;

        @Scheduled(cron = "0 0/1 * * * *")
        public void trade() {
                MinuteCandles.Request request = MinuteCandles.Request.builder()
                        .market("KRW-BTC")
                        .unit("60")
                        .count("100")
                        .build();
        }
}
