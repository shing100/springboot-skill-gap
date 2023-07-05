package comn.kingname.hangangview.util;

import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class Strategy {
    private static List<Map<String, Double>> df = new ArrayList<>();

    public static List<Map<String, Double>> getOhlcvData(String interval, int count) {
        // Implement this function
        return null;
    }

    public static List<Map<String, Double>> calculateTr(List<Map<String, Double>> series) {
        double prevClose = series.get(0).get("close");
        for (Map<String, Double> row : series) {
            double high = row.get("high");
            double low = row.get("low");
            double close = row.get("close");
            double tr = Math.max(high - low, Math.max(Math.abs(high - prevClose), Math.abs(low - prevClose)));
            row.put("tr", tr);
            prevClose = close;
        }
        return series;
    }

    public static List<Map<String, Double>> calculateAtr(List<Map<String, Double>> series, int AP) {
        // Implement this function
        return null;
    }

    public static List<Map<String, Double>> calculateBoundaries(List<Map<String, Double>> series, double coeff) {
        // Implement this function
        return null;
    }

    public static List<Map<String, Double>> calculateMfi(List<Map<String, Double>> series) {
        // Implement this function
        return null;
    }

    public static List<Map<String, Double>> calculateAlphaTrend(List<Map<String, Double>> series) {
        // Implement this function
        return null;
    }

    public static List<Map<String, Double>> calculateSignals(List<Map<String, Double>> series) {
        // Implement this function
        return null;
    }
}
