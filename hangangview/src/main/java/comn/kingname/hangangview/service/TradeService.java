package comn.kingname.hangangview.service;

import comn.kingname.hangangview.domain.Candle;
import comn.kingname.hangangview.domain.OrderType;
import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.dto.Orders;
import comn.kingname.hangangview.exception.TradeErrorCode;
import comn.kingname.hangangview.exception.TradeException;
import comn.kingname.hangangview.util.Strategy;
import comn.kingname.hangangview.util.TelegramSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static comn.kingname.hangangview.contants.Constants.*;

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
        Candle latestCandle = candles.get(candles.size() - 1);

        double coinBalance = getCoinBalance(request);   // 코인 정보 가져오기
        double currentPrice = marketService.getTicker(request.getMarket()).get(0).getTrade_price(); // 현재가 가져오기
        log.info("market = {}, coinBalance = {}, currentPrice = {}, latestCandle buy = {}, sell = {}", request.getMarket(), coinBalance, currentPrice, latestCandle.isBuySignal(), latestCandle.isSellSignal());

        if (changeSignal(candles)) {
            if (latestCandle.isBuySignal()) {
                buyCoin(request);
            }

            if (latestCandle.isSellSignal()) {
                sellCoin(request, coinBalance, currentPrice);
            }
        }

        if (coinBalance > 0 && coinBalance * STOP_LOSS_RATE > currentPrice) {
            // TODO 수수료 계산 추가 필요
            Orders.Response response = marketService.marketOrder(request.getMarket(), Double.parseDouble(accountService.getBalance(request.getMarket()).getBalance()) * 0.9995, OrderType.ASK);
            if (Objects.nonNull(response)) {
                telegramSender.sendMessage(request.getMarket() + " Stoploss 완료 금액 : " + response.getPrice() + " " + response.getExecuted_volume());
            } else {
                telegramSender.sendMessage(request.getMarket() + " Stoploss 실패: 주문 결과를 받지 못했습니다.");
            }
        }
    }

    // 마지막 캔들 변화가 있을 때만 매수/매도
    private boolean changeSignal(List<Candle> candles) {
        Candle latestCandle = candles.get(candles.size() - 1);
        Candle beforeCandle = candles.get(candles.size() - 2);
        return latestCandle.isBuySignal() != beforeCandle.isBuySignal() || latestCandle.isSellSignal() != beforeCandle.isSellSignal();
    }

    public void sellCoin(MinuteCandles.Request request, double coinBalance, double currentPrice) {
        if (coinBalance > 0) {
            log.info("매도 주문 : " + request.getMarket() + " " + coinBalance + "개");
            double avgPrice = getAvgCoinPrice(request);
            if (avgPrice * coinBalance > MIN_BUY_AMOUNT && avgPrice * PROFITABILITY_RATE >= currentPrice) {
                // TODO 수수료 계산 추가 필요
                Orders.Response sellMarketOrder = marketService.marketOrder(request.getMarket(), coinBalance * 0.9995, OrderType.ASK);
                if (Objects.nonNull(sellMarketOrder)) {
                    telegramSender.sendMessage(request.getMarket() + " 매도 완료 금액 : " + sellMarketOrder.getPrice());
                } else {
                    telegramSender.sendMessage(request.getMarket() + "매수 실패: 주문 결과를 받지 못했습니다.");
                }
            }
        }
    }

    public void buyCoin(MinuteCandles.Request request) {
        double krw = Double.parseDouble(accountService.getBalance("KRW").getBalance());
        if (krw > MIN_BUY_AMOUNT) {
            log.info(krw + "원으로 " + request.getMarket() + " 매수");
            Orders.Response buyMarketOrder = marketService.marketOrder(request.getMarket(), krw * 0.9995, OrderType.BID);
            if (Objects.nonNull(buyMarketOrder)) {
                telegramSender.sendMessage(request.getMarket() + " 매수 완료 금액 : " + buyMarketOrder.getPrice());
            } else {
                telegramSender.sendMessage(request.getMarket() + "매수 실패: 주문 결과를 받지 못했습니다.");
            }
        }
    }

    private double getCoinBalance(MinuteCandles.Request request) {
        double coinBalance = 0;
        try {
            coinBalance = Double.parseDouble(accountService.getBalance(request.getMarket().split("-")[1]).getBalance());
        } catch (TradeException e) {
            if (e.getErrorCode() == TradeErrorCode.ACCOUNT_NOT_FOUND) {
                coinBalance = 0;
            }
        }
        return coinBalance;
    }

    private double getAvgCoinPrice(MinuteCandles.Request request) {
        try {
            return Double.parseDouble(accountService.getBalance(request.getMarket().split("-")[1]).getAvg_buy_price());
        } catch (TradeException e) {
            if (e.getErrorCode() == TradeErrorCode.ACCOUNT_NOT_FOUND) {
                return 0;
            }
        }
        return 0;
    }

}
