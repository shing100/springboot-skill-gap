package comn.kingname.hangangview;

import comn.kingname.hangangview.service.AccountService;
import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class HangangviewApplication {

    private final TelegramSender telegramSender;
    private final AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(HangangviewApplication.class, args);
    }
}
