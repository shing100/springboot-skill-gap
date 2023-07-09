package comn.kingname.hangangview.dto;

import lombok.*;

public class MarketTicker {

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder @ToString
    public static class Request {
        @NonNull
        private String markets;
    }

    @Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
    public static class Response {
        /*
        market	종목 구분 코드	String
        trade_date	최근 거래 일자(UTC)
        포맷: yyyyMMdd	String
        trade_time	최근 거래 시각(UTC)
        포맷: HHmmss	String
        trade_date_kst	최근 거래 일자(KST)
        포맷: yyyyMMdd	String
        trade_time_kst	최근 거래 시각(KST)
        포맷: HHmmss	String
        trade_timestamp	최근 거래 일시(UTC)
        포맷: Unix Timestamp	Long
        opening_price	시가	Double
        high_price	고가	Double
        low_price	저가	Double
        trade_price	종가(현재가)	Double
        prev_closing_price	전일 종가(UTC 0시 기준)	Double
        change
        change_price	변화액의 절대값	Double
        change_rate	변화율의 절대값	Double
        signed_change_price	부호가 있는 변화액	Double
        signed_change_rate	부호가 있는 변화율	Double
        trade_volume	가장 최근 거래량	Double
        acc_trade_price	누적 거래대금(UTC 0시 기준)	Double
        acc_trade_price_24h	24시간 누적 거래대금	Double
        acc_trade_volume	누적 거래량(UTC 0시 기준)	Double
        acc_trade_volume_24h	24시간 누적 거래량	Double
        highest_52_week_price	52주 신고가	Double
        highest_52_week_date	52주 신고가 달성일
        포맷: yyyy-MM-dd	String
        lowest_52_week_price	52주 신저가	Double
        lowest_52_week_date	52주 신저가 달성일
        포맷: yyyy-MM-dd	String
        timestamp	타임스탬프	Long
         */
        private String market;
        private String trade_date;
        private String trade_time;
        private String trade_date_kst;
        private String trade_time_kst;
        private Long trade_timestamp;
        private double opening_price;
        private double high_price;
        private double low_price;
        private double trade_price;
        private double prev_closing_price;
        private String change;      //EVEN : 보합 RISE : 상승 FALL : 하락
        private double change_price;
        private double change_rate;
        private double signed_change_price;
        private double signed_change_rate;
        private double trade_volume;
        private double acc_trade_price;
        private double acc_trade_price_24h;
        private double acc_trade_volume;
        private double acc_trade_volume_24h;
        private double highest_52_week_price;
        private String highest_52_week_date;
        private double lowest_52_week_price;
        private String lowest_52_week_date;
        private Long timestamp;
    }
}
