package comn.kingname.hangangview.contoller;

import comn.kingname.hangangview.domain.Account;
import comn.kingname.hangangview.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts")
    public List<Account.Response> getAccount() {
        return accountService.getAccounts();
    }



}
