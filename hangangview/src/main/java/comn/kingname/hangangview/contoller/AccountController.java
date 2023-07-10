package comn.kingname.hangangview.contoller;

import comn.kingname.hangangview.contants.Constants;
import comn.kingname.hangangview.dto.Account;
import comn.kingname.hangangview.scheduler.TradeScheduler;
import comn.kingname.hangangview.service.AccountService;
import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TelegramSender telegramSender;

    @GetMapping("/accounts")
    public List<Account.Response> getAccount() {
        log.info("GET /accounts");
        telegramSender.sendMessage(accountService.getAccounts().stream().map(
                account -> "현재 계좌 잔고: " + account.getCurrency() + " " + account.getBalance()
        ).collect(Collectors.toList()).toString());
        return accountService.getAccounts();
    }

    @GetMapping("schedule/on")
    public String scheduleOn() {
        log.info("GET /schedule/on");
        Constants.IS_TRADE = true;
        telegramSender.sendMessage("거래 시작");
        return "거래 시작";
    }

    @GetMapping("schedule/off")
    public String scheduleOff() {
        log.info("GET /schedule/off");
        Constants.IS_TRADE = false;
        telegramSender.sendMessage("거래 중지");
        return "거래 중지";
    }

}
