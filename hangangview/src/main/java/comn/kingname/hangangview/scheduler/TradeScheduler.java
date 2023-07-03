package comn.kingname.hangangview.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TradeScheduler {

        @Scheduled(cron = "0 0/1 * * * *")
        public void trade() {
            log.info("trade");
        }
}
