package comn.kingname.hangangview.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import comn.kingname.hangangview.domain.Candle;
import comn.kingname.hangangview.dto.MinuteCandles;
import comn.kingname.hangangview.service.MarketService;
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

    public static boolean isBuySignal() {
        return false;
    }

    public List<Candle> getOhlcvData(MinuteCandles.Request request) {
        return marketService.getMinuteCandles(request).stream().map(o -> objectMapper.convertValue(o, Candle.class)).collect(Collectors.toList());
    }

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

    public List<Candle> calculateAtr(List<Map<String, Double>> candles, int AP) {
        // Implement this function
        return null;
    }

    public List<Candle> calculateBoundaries(List<Candle> candles, double coeff) {
        // Implement this function
        return null;
    }

    public List<Candle> calculateMfi(List<Candle> candles) {
        // Implement this function
        return null;
    }

    public List<Candle> calculateAlphaTrend(List<Candle> candles) {
        // Implement this function
        return null;
    }

    public List<Candle> calculateSignals(List<Candle> candles) {
        // Implement this function
        return null;
    }
}
