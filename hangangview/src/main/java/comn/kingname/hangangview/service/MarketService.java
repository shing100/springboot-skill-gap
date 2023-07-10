package comn.kingname.hangangview.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import comn.kingname.hangangview.domain.MarketCode;
import comn.kingname.hangangview.domain.OrderType;
import comn.kingname.hangangview.dto.MarketTicker;
import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.dto.Orders;
import comn.kingname.hangangview.dto.OrdersChance;
import comn.kingname.hangangview.exception.TradeErrorCode;
import comn.kingname.hangangview.exception.TradeException;
import comn.kingname.hangangview.properties.UpbitProperties;
import comn.kingname.hangangview.util.TelegramSender;
import comn.kingname.hangangview.util.TokenHeaderProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketService {

    private final TokenHeaderProvider tokenHeaderProvider;
    private final TelegramSender telegramSender;
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

        log.debug("response: {}", response.getBody());

        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }


    public List<MarketTicker.Response> getTicker(String... markets) {
        String query = String.join(",", markets);
        URI uri = URI.create(properties.getApiUrl() + "v1/ticker?markets=" + query);

        ResponseEntity<List<MarketTicker.Response>> response = restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(tokenHeaderProvider.getHeaders()), new ParameterizedTypeReference<List<MarketTicker.Response>>() {}
        );

        log.debug("response: {}", response.getBody());
        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }

    public OrdersChance.Response getOrdersChance(OrdersChance.Request request) {
        URI uri = URI.create(properties.getApiUrl() + "v1/orders/chance?market=" + request.getMarket());

        ResponseEntity<OrdersChance.Response> response = restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(tokenHeaderProvider.getHeaders()), new ParameterizedTypeReference<OrdersChance.Response>() {}
        );

        log.debug("response: {}", response.getBody());
        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }

    @SneakyThrows
    public Orders.Response marketOrder(String market, double volume, OrderType orderType) {
        //OrdersChance.Response ordersChance = getOrdersChance(OrdersChance.Request.builder().market(market).build());
        //log.info(ordersChance.toString());

        HashMap<String, String> params = new HashMap<>();
        params.put("market", market);
        params.put("side", orderType.getType());

        switch (orderType) {
            case ASK: // 매도
                params.put("volume", String.valueOf(volume)); // 매도수량 필수
                params.put("ord_type", "market"); // 시장가 매도
                break;
            case BID: // 매수
                params.put("price", String.valueOf(volume)); // 매수 가격
                params.put("ord_type", "price" ); // 시장가 매수
                break;
            default:
                return null;
        }

        log.info(params.toString());
        ResponseEntity<Orders.Response> response = restTemplate.exchange(
                properties.getApiUrl() + "v1/orders", HttpMethod.POST,
                new HttpEntity<>(params, tokenHeaderProvider.getJwtTokenHeaders(params)), new ParameterizedTypeReference<Orders.Response>() {}
        );

        log.info("response: {}", response.getBody());

        if (response.getStatusCode().isError()) {
            throw new TradeException(TradeErrorCode.UPBIT_SERVER_ERROR);
        }

        return response.getBody();
    }
}
