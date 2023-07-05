package comn.kingname.hangangview.service;

import comn.kingname.hangangview.util.TokenHeaderProvider;
import comn.kingname.hangangview.domain.Account;
import comn.kingname.hangangview.exception.TradeErrorCode;
import comn.kingname.hangangview.exception.TradeException;
import comn.kingname.hangangview.properties.UpbitProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final UpbitProperties properties;
    private final TokenHeaderProvider tokenHeaderProvider;
    private final RestTemplate restTemplate;

    public List<Account.Response> getAccounts() {
        ResponseEntity<List<Account.Response>> response = restTemplate.exchange(
                properties.getApiUrl() + "/v1/accounts", HttpMethod.GET,
                new HttpEntity<>(tokenHeaderProvider.getHeaders()), new ParameterizedTypeReference<List<Account.Response>>() {
        });

        log.debug("response : {}", response);
        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }
}
