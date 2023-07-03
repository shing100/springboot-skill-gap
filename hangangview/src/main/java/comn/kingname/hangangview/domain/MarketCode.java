package comn.kingname.hangangview.domain;


import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class MarketCode {

    private String market;				// 업비트에서 제공중인 시장 정보	String
    private String korean_name;			// 거래 대상 디지털 자산 한글명	    String
    private String english_name;		//	거래 대상 디지털 자산 영문명	String
    private String market_warning;		// 유의 종목 여부 NONE (해당 사항 없음), CAUTION(투자유의)	String
}
