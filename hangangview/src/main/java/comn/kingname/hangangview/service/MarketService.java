package comn.kingname.hangangview.service;

import comn.kingname.hangangview.domain.MarketCode;
import comn.kingname.hangangview.domain.MinuteCandles;
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
import org.springframework.web.util.UriBuilder;

import java.net.URI;
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
                properties.getApiUrl() + "v1/market/all", HttpMethod.GET,
                new HttpEntity<>(tokenHeaderProvider.getHeaders()), new ParameterizedTypeReference<List<MarketCode>>() {}
        );

        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }

    public List<MinuteCandles.Response> getMinuteCandles(MinuteCandles.Request request) {
        URI uri = URI.create(properties.getApiUrl() + "v1/candles/minutes/" + request.getUnit() + "?market=" + request.getMarket() + "&count=" + request.getCount());

        ResponseEntity<List<MinuteCandles.Response>> response = restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(tokenHeaderProvider.getHeaders()), new ParameterizedTypeReference<List<MinuteCandles.Response>>() {}
        );

        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }
}
