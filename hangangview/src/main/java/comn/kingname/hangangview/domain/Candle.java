package comn.kingname.hangangview.domain;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @ToString
public class Candle {
    private String market;                // 업비트에서 제공중인 시장 정보	String
    private String candle_date_time_utc;    // 캔들 기준 시각(UTC 기준)	String
    private String candle_date_time_kst;    // 캔들 기준 시각(KST 기준)	String
    private double opening_price;        // 시가	Double
    private double high_price;            // 고가	Double
    private double low_price;            // 저가	Double
    private double trade_price;            // 종가	Double
    private Long timestamp;            // 해당 캔들에서 마지막 틱이 저장된 시각	Long
    private double candle_acc_trade_price;    // 누적 거래 금액	Double
    private double candle_acc_trade_volume;    // 누적 거래량	Double
    private Integer unit;                // 분 단위(유닛)	Integer

    private double tr;
    private double atr;
    private double atrRate;

    private double upT;
    private double downT;
    private double mfi;
    private double alphaTrend;

    private int K1;
    private int K2;
    private int O1;
    private int O2;

    private boolean buySignal;
    private boolean sellSignal;

    private boolean finalBuySignal;
    private boolean finalSellSignal;

}
