package comn.kingname.hangangview.service;

import comn.kingname.hangangview.domain.Candle;
import comn.kingname.hangangview.dto.MarketTicker;
import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.dto.Orders;
import comn.kingname.hangangview.exception.TradeErrorCode;
import comn.kingname.hangangview.exception.TradeException;
import comn.kingname.hangangview.util.Strategy;
import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final MarketService marketService;
    private final AccountService accountService;
    private final TelegramSender telegramSender;
    private final Strategy strategy;

    public void executeOrders(MinuteCandles.Request request) {
        List<Candle> candles = strategy.getSignal(request);
        log.info("candles size = {}", candles.size());
        Candle latestCandle = candles.get(candles.size() - 1);

        // 코인 정보 가져오기
        double coinBalance = getCoinBalance(request);
        // 현재가 가져오기
        double currentPrice = marketService.getTicker(request.getMarket()).get(0).getTrade_price();

        log.info("currentPrice = {}", currentPrice);
        log.info("latestCandle = {}", latestCandle);

        if (latestCandle.isBuySignal()) {
            double krw = Double.parseDouble(accountService.getBalance("KRW").getBalance());
            if (krw > 5000) {
                Orders.Response buyMarketOrder = marketService.buyMarketOrder(request.getMarket(), krw * 0.9995);
                if (Objects.nonNull(buyMarketOrder)) {
                    telegramSender.sendMessage(request.getMarket() + " 매수 완료 " + buyMarketOrder.getOrd_type() + " " + buyMarketOrder.getVolume() + " " + buyMarketOrder.getPrice());
                } else {
                    telegramSender.sendMessage(request.getMarket() + "매수 실패: 주문 결과를 받지 못했습니다.");
                }
            }
        }

        if (latestCandle.isSellSignal() & coinBalance > 0) {
            double avgPrice = Double.parseDouble(accountService.getBalance(request.getMarket()).getAvg_buy_price());
            if (avgPrice * coinBalance > 5000 && avgPrice * 1.02 < currentPrice) {
                Orders.Response sellMarketOrder = marketService.sellMarketOrder(request.getMarket(), coinBalance * 0.9995);
                if (Objects.nonNull(sellMarketOrder)) {
                    telegramSender.sendMessage(request.getMarket() + " 매도 완료 " + sellMarketOrder.getOrd_type() + " " + sellMarketOrder.getVolume() + " " + sellMarketOrder.getPrice());
                } else {
                    telegramSender.sendMessage(request.getMarket() + "매수 실패: 주문 결과를 받지 못했습니다.");
                }
            }
        }

        if (coinBalance > 0 && coinBalance * 0.93 > currentPrice) {
            Orders.Response response = marketService.sellMarketOrder(request.getMarket(), Double.parseDouble(accountService.getBalance(request.getMarket()).getBalance()) * 0.9995);
            if (Objects.nonNull(response)) {
                telegramSender.sendMessage(request.getMarket() + " Stoploss 완료" + response.getOrd_type() + " " + response.getVolume() + " " + response.getPrice());
            } else {
                telegramSender.sendMessage(request.getMarket() + " Stoploss 실패: 주문 결과를 받지 못했습니다.");
            }
        }
    }

    private double getCoinBalance(MinuteCandles.Request request) {
        double coinBalance = 0;
        try {
            coinBalance = Double.parseDouble(accountService.getBalance(request.getMarket()).getBalance());
        } catch (TradeException e) {
            if (e.getErrorCode() == TradeErrorCode.ACCOUNT_NOT_FOUND) {
                coinBalance = 0;
            }
        }
        return coinBalance;
    }

}
