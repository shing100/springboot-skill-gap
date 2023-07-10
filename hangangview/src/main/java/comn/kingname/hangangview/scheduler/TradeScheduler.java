package comn.kingname.hangangview.scheduler;

import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.service.AccountService;
import comn.kingname.hangangview.service.TradeService;
import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static comn.kingname.hangangview.contants.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeScheduler {

        private final TradeService tradeService;
        private final AccountService accountService;
        private final TelegramSender telegramSender;

        // 20초 마다 실행
        @Scheduled(cron = "0/5 * * * * *")
        public void trade() {
            if (IS_TRADE) {
                MinuteCandles.Request request = MinuteCandles.Request.builder()
                        .market(MARKET)
                        .unit(UNIT_COUNT)
                        .count("100")
                        .build();

                tradeService.executeOrders(request);
            }
        }

        // 1시간 마다 실행
        @Scheduled(cron = "0 0 0/3 * * *")
        public void healthCheck() {
            telegramSender.sendMessage("현재 " + (IS_TRADE ? "거래중" : "거래중지"));
        }
}
