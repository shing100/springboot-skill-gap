package comn.kingname.hangangview.service;

import comn.kingname.hangangview.domain.MarketCode;
import comn.kingname.hangangview.exception.TradeErrorCode;
import comn.kingname.hangangview.exception.TradeException;
import comn.kingname.hangangview.properties.UpbitProperties;
import comn.kingname.hangangview.util.TokenHeaderProvider;
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
public class MarketService {

    private final TokenHeaderProvider tokenHeaderProvider;
    private final UpbitProperties properties;
    private final RestTemplate restTemplate;

    public List<MarketCode> getMarketAll() {
        ResponseEntity<List<MarketCode>> response = restTemplate.exchange(
                properties.getApiUrl() + "v1/market/all", HttpMethod.GET, new HttpEntity<>(tokenHeaderProvider.getHeaders()), new ParameterizedTypeReference<List<MarketCode>>() {}
        );

        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }
}
