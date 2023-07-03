package comn.kingname.hangangview.domain;

import lombok.*;

public class OrdersChance {

    @Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
    public static class Request {
        private String market;      // 마켓 코드 (ex. KRW-BTC)
    }

    @Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
    public static class Response {
        private String bid_fee;             // 매수 수수료 비율	NumberString
        private String ask_fee;             // 매도 수수료 비율	NumberString
        private Market market;              // 마켓에 대한 정보	Object
        private Object bid_account;         // 매수 시 사용하는 화폐의 계좌 상태	Object
        private Object ask_account;         // 매도 시 사용하는 화폐의 계좌 상태	Object

        @Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
        public static class Market {
            private String id;              // 마켓의 유일 키	String
            private String name;            // 마켓 이름	String
            private String[] order_types;     // 지원 주문 방식 배열	String[]
            private String[] order_sides;     // 지원 주문 종류 배열	String[]
            private String bid;             // 매수 시 제약사항	Object
            private String ask;             // 매도 시 제약사항	Object
            private String max_total;       // 최대 매도/매수 금액	NumberString
            private String state;           // 거래 가능 여부	String
        }
    }

}
