package comn.kingname.hangangview.domain;

import lombok.*;

public class MinuteCandles {

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
    public static class Response {
        private String market;                // 업비트에서 제공중인 시장 정보	String
        private String candle_date_time_utc;    // 캔들 기준 시각(UTC 기준)	String
        private String candle_date_time_kst;    // 캔들 기준 시각(KST 기준)	String
        private String opening_price;        // 시가	Double
        private String high_price;            // 고가	Double
        private String low_price;            // 저가	Double
        private String trade_price;            // 종가	Double
        private String timestamp;            // 해당 캔들에서 마지막 틱이 저장된 시각	Long
        private String candle_acc_trade_price;    // 누적 거래 금액	Double
        private String candle_acc_trade_volume;    // 누적 거래량	Double
        private String unit;                // 분 단위(유닛)	Integer
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @ToString @Builder
    public static class Request {
        private String market;      // 마켓 코드 (ex. KRW-BTC)
        private String to;          // 없을 경우 가장 최신 캔들
        private String count;       // max 200
    }
}
