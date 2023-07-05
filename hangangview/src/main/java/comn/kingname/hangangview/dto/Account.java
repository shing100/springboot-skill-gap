package comn.kingname.hangangview.dto;

import lombok.*;


public class Account {

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor @ToString
    public static class Response {
        private String currency;			    // 화폐를 의미하는 영문 대문자 코드	String
        private String balance;			        // 주문가문 금액/수량	NumberString
        private String locked;				    // 주문 중 묶여있는 금액/수량	NumberString
        private String avg_buy_price;			// 매수평균가	NumberString
        private Boolean avg_buy_price_modified;	// 매수평균가 수정 여부	Boolean
        private String unit_currency;			// 평단가 기준 화폐	String
    }

}
