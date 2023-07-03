package comn.kingname.hangangview.domain;

import lombok.*;


public class Orders {

    @Getter @Setter @AllArgsConstructor
    @NoArgsConstructor @ToString @Builder
    public static class Request {
        private String market;      // 마켓 코드 (ex. KRW-BTC)
        private String side;        // 주문 종류 (bid : 매수, ask : 매도)
        private String volume;      // 주문량 (지정가, 시장가 매도 시 필수)
        private String price;       // 주문 가격. (지정가, 시장가 매수 시 필수)
        private String ord_type;    // 주문 타입 (limit : 지정가 주문, price : 시장가 주문(매수), market : 시장가 주문(매도))
    }


    @Getter @Setter @AllArgsConstructor
    @NoArgsConstructor @ToString
    public static class Response {
        private String uuid;            // 주문의 고유 아이디	String
        private String side;            // 주문 종류	String
        private String ord_type;        // 주문 방식	String
        private String price;           // 주문 당시 화폐 가격	NumberString
        private String state;           // 주문 상태	String
        private String market;          // 마켓의 유일키	String
        private String created_at;      // 주문 생성 시간	String
        private String volume;          // 사용자가 입력한 주문 양	NumberString
        private String remaining_volume;// 체결 후 남은 주문 양	NumberString
        private String reserved_fee;    // 수수료로 예약된 비용	NumberString
        private String remaining_fee;   // 남은 수수료	NumberString
        private String paid_fee;        // 사용된 수수료	NumberString
        private String locked;          // 거래에 사용중인 비용	NumberString
        private String executed_volume; // 체결된 양	NumberString
        private String trade_count;     // 해당 주문에 걸린 체결 수	Integer
    }
}
