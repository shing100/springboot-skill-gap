package comn.kingname.hangangview.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import comn.kingname.hangangview.domain.Candle;
import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.service.MarketService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Strategy {

    private final MarketService marketService;
    private final ObjectMapper objectMapper;

    public List<Candle> getSignal(MinuteCandles.Request request) {
        List<Candle> ohlcvData = getOhlcvData(request);
        List<Candle> candles = calculateTr(ohlcvData);
        candles = calculateAtr(candles, 14);
        candles = calculateBoundaries(candles, 1.0);
        candles = calculateMfi(candles, 14);
        candles = calculateAlphaTrend(candles);
        candles = calculateSignals(candles);
        // calculateK(candles);
        // calculateO(candles);
        // calculateFinalSignals(candles);
        return candles;
    }


    public List<Candle> getOhlcvData(MinuteCandles.Request request) {
        return marketService.getMinuteCandles(request).stream().map(o -> objectMapper.convertValue(o, Candle.class)).collect(Collectors.toList());
    }

    // 캔들에 대해 True Range를 계산
    public List<Candle> calculateTr(List<Candle> candles) {
        double prevClose = candles.get(0).getTrade_price();
        for (Candle candle : candles) {
            double high = candle.getHigh_price();
            double low = candle.getLow_price();
            double close = candle.getTrade_price();
            double tr = Math.max(high - low, Math.max(Math.abs(high - prevClose), Math.abs(low - prevClose)));
            candle.setTr(tr);
            prevClose = close;
        }
        return candles;
    }


    /*
     캔들에 대해 True Range를 계산하고, 이를 사용하여 주어진 기간동안의 평균 True Range를 계산
     */
    public List<Candle> calculateAtr(List<Candle> candles, int AP) {
        double[] tr = new double[candles.size()];
        for (int i = 1; i < candles.size(); i++) {
            double highLow = candles.get(i).getHigh_price() - candles.get(i).getLow_price();
            double highClose = Math.abs(candles.get(i).getHigh_price() - candles.get(i - 1).getTrade_price());
            double lowClose = Math.abs(candles.get(i).getLow_price() - candles.get(i - 1).getTrade_price());
            tr[i] = Math.max(Math.max(highLow, highClose), lowClose);
            candles.get(i).setTr(tr[i]);
        }

        for (int i = AP; i < candles.size(); i++) {
            double sum = 0;
            for (int j = i - AP; j < i; j++) {
                sum += tr[j];
            }
            candles.get(i).setAtr(sum / AP);
        }

        return candles;
    }

    /*
     각 캔들에 대해 상한선(upT)과 하한선(downT)을 계산하고, 이를 각 캔들의 upT와 downT 필드에 저장
     */
    public List<Candle> calculateBoundaries(List<Candle> candles, double coeff) {
        for (Candle candle : candles) {
            double upT = candle.getLow_price() - candle.getAtr() * coeff;
            double downT = candle.getHigh_price() + candle.getAtr() * coeff;
            candle.setUpT(upT);
            candle.setDownT(downT);
        }
        return candles;
    }


    /*
    각 캔들에 대해 MFI를 계산하고, 이를 각 캔들의 mfi 필드에 저장합니다. 이 값은 주어진 기간동안의 긍정적인 돈 흐름과 부정적인 돈 흐름의 비율을 이용하여 계산
    window = 14
     */
    public List<Candle> calculateMfi(List<Candle> candles, int window) {
        double[] typicalPrice = new double[candles.size()];
        double[] moneyFlow = new double[candles.size()];
        double[] positiveMoneyFlow = new double[candles.size()];
        double[] negativeMoneyFlow = new double[candles.size()];

        for (int i = 0; i < candles.size(); i++) {
            typicalPrice[i] = (candles.get(i).getHigh_price() + candles.get(i).getLow_price() + candles.get(i).getTrade_price()) / 3;
            moneyFlow[i] = typicalPrice[i] * candles.get(i).getCandle_acc_trade_volume();
            if (i > 0) {
                positiveMoneyFlow[i] = (typicalPrice[i] > typicalPrice[i - 1]) ? moneyFlow[i] : 0;
                negativeMoneyFlow[i] = (typicalPrice[i] < typicalPrice[i - 1]) ? moneyFlow[i] : 0;
            }
        }

        for (int i = window; i < candles.size(); i++) {
            double positiveMoneyFlowSum = 0;
            double negativeMoneyFlowSum = 0;
            for (int j = i - window; j < i; j++) {
                positiveMoneyFlowSum += positiveMoneyFlow[j];
                negativeMoneyFlowSum += negativeMoneyFlow[j];
            }
            double moneyFlowRatio = positiveMoneyFlowSum / negativeMoneyFlowSum;
            candles.get(i).setMfi(100 - (100 / (1 + moneyFlowRatio)));
        }

        return candles;
    }


    public static List<Candle> calculateK(List<Candle> candles) {
        int buyGroup = 0;
        int sellGroup = 0;
        for (int i = 1; i < candles.size(); i++) {
            if (candles.get(i).isBuySignal() != candles.get(i - 1).isBuySignal()) {
                buyGroup++;
            }
            if (candles.get(i).isSellSignal() != candles.get(i - 1).isSellSignal()) {
                sellGroup++;
            }
            candles.get(i).setK1(candles.get(i).isBuySignal() ? buyGroup : 0);
            candles.get(i).setK2(candles.get(i).isSellSignal() ? sellGroup : 0);
        }

        return candles;
    }

    public static List<Candle> calculateO(List<Candle> candles) {
        int buyGroup = 0;
        int sellGroup = 0;
        for (int i = 1; i < candles.size(); i++) {
            if (candles.get(i).isBuySignal() != candles.get(i - 1).isBuySignal()) {
                buyGroup++;
            }
            if (candles.get(i).isSellSignal() != candles.get(i - 1).isSellSignal()) {
                sellGroup++;
            }
            candles.get(i).setO1(candles.get(i).isBuySignal() ? buyGroup : 0);
            candles.get(i).setO2(candles.get(i).isSellSignal() ? sellGroup : 0);
        }

        return candles;
    }

    /*
    각 캔들에 대해 alphaTrend를 계산하고, 이를 각 캔들의 alphaTrend 필드에 저장합니다.
     */
    public List<Candle> calculateAlphaTrend(List<Candle> candles) {
        candles.get(0).setAlphaTrend(0);
        for (int i = 1; i < candles.size(); i++) {
            double mfi = candles.get(i).getMfi();
            double upT = candles.get(i).getUpT();
            double downT = candles.get(i).getDownT();
            double previousAlphaTrend = candles.get(i - 1).getAlphaTrend();
            if (mfi >= 50.0 && upT < previousAlphaTrend) {
                candles.get(i).setAlphaTrend(previousAlphaTrend);
            } else {
                candles.get(i).setAlphaTrend(upT);
            }
            if (mfi < 50.0 && downT > previousAlphaTrend) {
                candles.get(i).setAlphaTrend(previousAlphaTrend);
            } else {
                candles.get(i).setAlphaTrend(downT);
            }
        }

        return candles;
    }

    public List<Candle> calculateSignals(List<Candle> candles) {
        for (int i = 2; i < candles.size(); i++) {
            double alphaTrend = candles.get(i).getAlphaTrend();
            double alphaTrend2DaysAgo = candles.get(i - 2).getAlphaTrend();
            candles.get(i).setBuySignal(alphaTrend > alphaTrend2DaysAgo && alphaTrend2DaysAgo != 0);
            candles.get(i).setSellSignal(alphaTrend < alphaTrend2DaysAgo && alphaTrend2DaysAgo != 0);
        }
        return candles;
    }

    public static List<Candle> calculateFinalSignals(List<Candle> candles) {
        for (Candle candle : candles) {
            candle.setFinalBuySignal(candle.isBuySignal() && candle.getO1() > candle.getK2());
            candle.setFinalSellSignal(candle.isSellSignal() && candle.getO2() > candle.getK1());
        }

        return candles;
    }
}
